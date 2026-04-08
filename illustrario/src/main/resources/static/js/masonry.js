document.addEventListener("DOMContentLoaded", () => {
  const container = document.querySelector(".gallery");
  const items = Array.from(container.children);

  const columnCount = 4;
  const columns = [];

  container.innerHTML = "";

  for (let i = 0; i < columnCount; i++) {
    const col = document.createElement("div");
    col.classList.add("masonry-column");
    container.appendChild(col);
    columns.push(col);
  }

  setTimeout(() => {
    items.forEach(item => {
      const smallest = columns.reduce((prev, curr) =>
        prev.offsetHeight < curr.offsetHeight ? prev : curr
      );

      smallest.appendChild(item);
    });
  }, 100);
});