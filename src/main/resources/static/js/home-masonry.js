document.addEventListener("DOMContentLoaded", () => {
  const wall = document.getElementById("homeMasonryWall");
  const track = document.getElementById("homeMasonryTrack");

  if (!wall || !track) return;

  const COLUMN_GAP = 14;
  let loading = false;
  let hasMore = track.dataset.hasMore === "true";
  let nextPage = Number.parseInt(track.dataset.nextPage || "1", 10);
  const pageSize = Number.parseInt(track.dataset.pageSize || "18", 10);
  let columns = [];

  const maxColumnHeight = () => Math.max(420, wall.clientHeight - 8);

  const createColumn = () => {
    const col = document.createElement("div");
    col.className = "home-masonry-column";
    track.appendChild(col);
    columns.push(col);
    return col;
  };

  const resetColumns = () => {
    track.innerHTML = "";
    columns = [];
  };

  const ensureColumn = (estimatedCardHeight) => {
    if (!columns.length) return createColumn();
    const current = columns[columns.length - 1];
    const predictedHeight = current.scrollHeight + estimatedCardHeight + COLUMN_GAP;
    if (current.children.length && predictedHeight > maxColumnHeight()) return createColumn();
    return current;
  };

  const estimateCardHeight = (card) => {
    const rectHeight = Math.round(card.getBoundingClientRect().height);
    if (rectHeight > 0) return rectHeight;
    return 280;
  };

  const placeCard = (card) => {
    const column = ensureColumn(estimateCardHeight(card));
    column.appendChild(card);
  };

  const markImageLoaded = (img) => {
    if (!img) return;
    img.classList.add("is-loaded");
  };

  const waitForImage = (card) =>
    new Promise((resolve) => {
      const img = card.querySelector("img");
      if (!img) {
        resolve();
        return;
      }

      img.classList.add("home-masonry-image");

      if (img.complete) {
        markImageLoaded(img);
        resolve();
        return;
      }

      const done = () => {
        markImageLoaded(img);
        resolve();
      };

      img.addEventListener("load", done, { once: true });
      img.addEventListener("error", done, { once: true });
    });

  const appendCards = async (cards) => {
    for (const card of cards) {
      // Add first so browser can compute card height from CSS/layout.
      placeCard(card);
      await waitForImage(card);
      // If image changed card height and overflowed too much, re-balance this card.
      const col = card.parentElement;
      if (col && col.scrollHeight > maxColumnHeight() + 120 && col.children.length > 1) {
        col.removeChild(card);
        placeCard(card);
      }
    }
  };

  const createCard = (artwork) => {
    const link = document.createElement("a");
    link.className = "masonry-card masonry-item";
    link.href = "/gallery/artwork/" + artwork.id;

    const img = document.createElement("img");
    img.className = "home-masonry-image";
    img.src = artwork.filePath;
    img.alt = artwork.title || "Obra";
    img.loading = "lazy";

    const overlay = document.createElement("div");
    overlay.className = "masonry-overlay";

    const title = document.createElement("p");
    title.className = "mo-title";
    title.textContent = artwork.title || "Sem título";

    overlay.appendChild(title);

    if (artwork.artistName) {
      const meta = document.createElement("p");
      meta.className = "mo-meta";
      meta.textContent = "por " + artwork.artistName;
      overlay.appendChild(meta);
    }

    link.appendChild(img);
    link.appendChild(overlay);

    return link;
  };

  const syncDataState = () => {
    track.dataset.hasMore = String(hasMore);
    track.dataset.nextPage = String(nextPage);
  };

  const loadMore = async () => {
    if (!hasMore || loading) return;
    loading = true;

    try {
      const response = await fetch("/api/home/artworks?page=" + nextPage + "&size=" + pageSize, {
        headers: { Accept: "application/json" }
      });

      if (!response.ok) throw new Error("Falha ao carregar obras.");
      const payload = await response.json();
      const artworks = Array.isArray(payload.artworks) ? payload.artworks : [];

      if (!artworks.length) {
        hasMore = false;
        syncDataState();
        return;
      }

      const cards = artworks.map(createCard);
      await appendCards(cards);

      hasMore = Boolean(payload.hasMore);
      nextPage = Number.isInteger(payload.nextPage) ? payload.nextPage : nextPage + 1;
      syncDataState();
    } catch (_error) {
      // Keep silent: user can keep browsing loaded content.
    } finally {
      loading = false;
    }
  };

  const maybeLoadMore = () => {
    const threshold = 600;
    if (wall.scrollLeft + wall.clientWidth >= wall.scrollWidth - threshold) {
      void loadMore();
    }
  };

  const initialCards = Array.from(track.querySelectorAll(".masonry-card"));

  const relayoutAll = async () => {
    const cards = Array.from(track.querySelectorAll(".masonry-card"));
    resetColumns();
    await appendCards(cards);
    maybeLoadMore();
  };

  const bootstrap = async () => {
    resetColumns();
    await appendCards(initialCards);

    // If content still fits in viewport, prefetch more pages.
    let guard = 0;
    while (hasMore && wall.scrollWidth <= wall.clientWidth + 80 && guard < 3) {
      await loadMore();
      guard += 1;
    }
  };

  let resizeTimer = null;

  wall.addEventListener("scroll", maybeLoadMore, { passive: true });
  wall.addEventListener(
    "wheel",
    (event) => {
      if (Math.abs(event.deltaY) > Math.abs(event.deltaX)) {
        wall.scrollLeft += event.deltaY;
        event.preventDefault();
      }
    },
    { passive: false }
  );

  window.addEventListener("resize", () => {
    if (resizeTimer) window.clearTimeout(resizeTimer);
    resizeTimer = window.setTimeout(() => {
      void relayoutAll();
    }, 180);
  });

  void bootstrap();
});
