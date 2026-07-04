document.addEventListener('DOMContentLoaded', () => {
  const addBtn = document.getElementById('add-btn');
  const titleInput = document.getElementById('title-input');
  const taskBody = document.getElementById('task-body');

  // Add task: POST /tasks?title=... → server returns rendered <tr> fragment.
  addBtn.addEventListener('click', async () => {
    const title = titleInput.value.trim();
    if (!title) return;

    const params = new URLSearchParams({ title });
    const response = await fetch('/tasks', { method: 'POST', body: params });

    if (response.ok) {
      const html = await response.text();
      // Remove the "No tasks yet" row if present.
      const emptyRow = taskBody.querySelector('tr td[colspan]');
      if (emptyRow) emptyRow.parentElement.remove();

      taskBody.insertAdjacentHTML('beforeend', html);
      titleInput.value = '';
    }
  });

  // Delete task: DELETE /tasks/{id} → 204; JS removes the row from the DOM.
  taskBody.addEventListener('click', async (e) => {
    if (!e.target.matches('.delete-btn')) return;

    const id = e.target.dataset.id;
    const response = await fetch(`/tasks/${id}`, { method: 'DELETE' });

    if (response.status === 204) {
      document.getElementById(`row-${id}`).remove();
    }
  });
});
