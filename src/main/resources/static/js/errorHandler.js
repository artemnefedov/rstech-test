export function handleApiError(response) {
	if (!response.ok) {
		return response.json().then(resp => {
			displayError(`
			Ошибка: ${response.status} 
			${resp.error}`);
		});
	}
	return response;
}

export function displayError(message) {
	const errorContainer = document.getElementById('error-container');
	if (!errorContainer) {
		console.error('Error container not found:', message);
		return;
	}

	const toastId = 'toast-' + Date.now(); // уникальный id
	const toastElement = document.createElement('div');
	toastElement.className = 'toast align-items-center text-bg-danger border-0 show';
	toastElement.setAttribute('role', 'alert');
	toastElement.setAttribute('aria-live', 'assertive');
	toastElement.setAttribute('aria-atomic', 'true');
	toastElement.setAttribute('id', toastId);

	toastElement.innerHTML = `
		<div class="d-flex">
			<div class="toast-body">
				${message}
			</div>
			<button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Закрыть"></button>
		</div>
	`;

	errorContainer.appendChild(toastElement);

	const toast = new bootstrap.Toast(toastElement, {delay: 5000});
	toast.show();

	toastElement.addEventListener('hidden.bs.toast', () => {
		toastElement.remove();
	});
}