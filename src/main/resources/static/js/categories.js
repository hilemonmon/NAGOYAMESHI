document.addEventListener('DOMContentLoaded', () => {
  const editModal = document.getElementById('editCategoryModal');
  if (editModal) {
    editModal.addEventListener('show.bs.modal', event => {
      const button = event.relatedTarget;
      const id = button.getAttribute('data-id');
      const name = button.getAttribute('data-name');
      const form = editModal.querySelector('form');
      form.action = `/admin/categories/${id}/update`;
      form.querySelector('input[name="name"]').value = name;
    });
  }

  const deleteModal = document.getElementById('deleteCategoryModal');
  if (deleteModal) {
    deleteModal.addEventListener('show.bs.modal', event => {
      const button = event.relatedTarget;
      const id = button.getAttribute('data-id');
      const name = button.getAttribute('data-name');
      const form = deleteModal.querySelector('form');
      form.action = `/admin/categories/${id}/delete`;
      deleteModal.querySelector('.modal-body span').textContent = name;
    });
  }
});
