import {checkAuth, logout} from './auth.js';
import {api} from './api.js';
import {Category, Product} from "./models.js";

checkAuth();

let totalPages;
let currentPage = 0;
let name;
let categoryName;
let priceFrom;
let priceTo;

const authorities = localStorage.getItem("authorities");
const container = document.getElementById('product-list');

const renderProductRow = (product) => {
	const tr = document.createElement('tr');
	let innerHTML = `
    <td>
    	<img src="${product.imageUrl}" alt="${product.name}" class="img-thumbnail" style="max-height: 100px;">
    </td>
    <td>${product.name}</td>
    <td class="text-muted small">${product.description}</td>
    <td><strong>${product.price}</strong></td>
    <td><span class="badge bg-secondary">${product.category.name}</span></td>
    <td>
      <span class="badge ${product.isActive ? 'bg-success' : 'bg-danger'}">
        ${product.isActive ? 'Активен' : 'Неактивен'}
      </span>
    </td>
  `;

	if (authorities.includes('update') || authorities.includes('delete')) {
		innerHTML += `
      <td class="text-end">
        <div class="d-flex justify-content-end gap-2">
    `;

		if (authorities.includes('update')) {
			innerHTML += `<button type="button" onclick="editProduct(${product.id})" class="btn btn-sm btn-outline-primary">Редактировать</button>`;
		}

		if (authorities.includes('delete')) {
			innerHTML += `
        <button type="button" onclick="deleteProduct(${product.id})" class="btn btn-sm btn-outline-danger">Удалить</button>
      `;
		}

		innerHTML += `
        </div>
      </td>
    `;
	}

	tr.innerHTML = innerHTML;

	return tr;
};

async function updatePagination() {

	const pagination = document.getElementById('pagination');
	if (totalPages <= 1) {
		pagination.innerHTML = '';
		return;
	}

	let pageList = '<ul class="pagination justify-content-center">';

	for (let i = 0; i < totalPages; i++) {
		if (i === currentPage) {
			pageList += `
			<li class="page-item active">
				<button class="page-link">${i + 1}</button>
			</li>`;
		} else {
			pageList += `
			<li class="page-item">
				<button class="page-link" onclick="changePage(${i})">${i + 1}</button>
			</li>`;
		}
	}
	pageList += '</ul>';

	pagination.innerHTML = pageList;
}

async function loadProducts() {
	return api.products.get({
		page: currentPage,
		name: name,
		categoryName: categoryName,
		priceFrom: priceFrom,
		priceTo: priceTo
	})
	.then(resp => {
		if (resp.status !== 200) {
			throw new Error('Ошибка при получении данных')
		} else {
			return resp.json();
		}
	})
	.then(data => {
		totalPages = data.totalPages;
		updatePagination();
		return data.products.map(product => new Product(
				product.id,
				product.name,
				product.description,
				product.price,
				product.imageUrl,
				new Category(
						product.category.id,
						product.category.name,
						product.category.description
				),
				product.createdAt,
				product.isActive
		));
	});
}

async function loadCategories() {
	return api.category.get()
	.then(resp => {
		if (resp.status !== 200) {
			throw new Error('Ошибка при получении данных')
		} else {
			return resp.json();
		}
	})
	.then(data => {
		return data.map(category => new Category(
				category.id,
				category.name,
				category.description
		));
	});
}

async function init() {

	if (authorities.includes('update') || authorities.includes('delete')) {
		const tableHeader = document.getElementById('table-header');
		tableHeader.innerHTML += `<th scope="col" class="text-end">Действия</th>`;
	}

	const [products, categories] = await Promise.all([
		loadProducts(),
		loadCategories()
	]);

	products.forEach(product => {
		container.appendChild(renderProductRow(product));
	});

	const categorySelect = document.getElementById('category-list');

	categories.forEach(category => {
		const option = document.createElement('option');
		option.value = category.name;
		option.textContent = category.name;
		categorySelect.appendChild(option);
	});
}

async function filter(page) {
	if (page) {
		currentPage = page;
	} else {
		currentPage = 0;
	}
	name = document.getElementById('name').value;
	categoryName = document.getElementById('category-list').value;
	priceFrom = document.getElementById('price-from').value;
	priceTo = document.getElementById('price-to').value;

	loadProducts().then(products => {
		container.innerHTML = '';
		products.forEach(product => {
			container.appendChild(renderProductRow(product));
		});
	});

}

async function changePage(page) {
	await Promise.all([
		filter(page),
		updatePagination()
	]);
}

async function formatDateForInput(dateString) {
	if (!dateString) {
		return '';
	}
	const date = new Date(dateString);
	return date.toISOString().split('T')[0];
}

async function editProduct(id) {

	const [productResp, categoriesResp] = await Promise.all([
		api.products.getById(id).then(resp => resp.json()),
		api.category.get().then(resp => resp.json())
	]);

	const modalBody = document.getElementById("edit-product-body");
	const formattedDate = await formatDateForInput(productResp.createdAt);

	const categoryOptions = categoriesResp.map(category => {
		const selected = category.id === productResp.category.id ? 'selected' : '';
		return `<option value="${category.id}" ${selected}>${category.name}</option>`;
	}).join('');

	modalBody.innerHTML = `
		<form id="edit-product-form">
			<div class="mb-3">
				<label for="product-name" class="form-label">Название</label>
				<input type="text" class="form-control" id="product-name" value="${productResp.name}">
			</div>
			<div class="mb-3">
				<label for="product-description" class="form-label">Описание</label>
				<textarea class="form-control" id="product-description">${productResp.description}</textarea>
			</div>
			<div class="mb-3">
				<label for="product-price" class="form-label">Цена</label>
				<input type="number" class="form-control" id="product-price" value="${productResp.price}" step="0.01">
			</div>
			<div class="mb-3">
				<label for="product-image" class="form-label">Ссылка на изображение</label>
				<input type="text" class="form-control" id="product-image" value="${productResp.imageUrl}">
				<div class="mt-2">
					<img src="${productResp.imageUrl}" alt="Предпросмотр" class="img-thumbnail" style="max-height: 100px;">
				</div>
			</div>
			<div class="mb-3">
				<label for="product-category" class="form-label">Категория</label>
				<select class="form-select" id="product-category">
					${categoryOptions}
				</select>
			</div>
			<div class="mb-3">
				<label for="product-created" class="form-label">Дата создания</label>
				<input type="date" class="form-control" id="product-created" value="${formattedDate}">
			</div>
			<div class="mb-3 form-check">
				<input type="checkbox" class="form-check-input" id="product-active" ${productResp.isActive
			? 'checked' : ''}>
				<label class="form-check-label" for="product-active">Активен</label>
			</div>
			<div class="d-flex justify-content-end">
				<button type="submit" class="btn btn-primary">Сохранить</button>
			</div>
		</form>
	`;

	const modal = new bootstrap.Modal(
			document.getElementById('editProductModal'));
	modal.show();

	document.getElementById('edit-product-form').addEventListener('submit',
			(e) => {
				e.preventDefault();

				const createdDate = document.getElementById('product-created').value;
				const createdAt = createdDate ? new Date(createdDate).toISOString()
						: productResp.createdAt;

				const updatedProduct = {
					id: productResp.id,
					name: document.getElementById('product-name').value,
					description: document.getElementById('product-description').value,
					price: parseFloat(document.getElementById('product-price').value),
					imageUrl: document.getElementById('product-image').value,
					category: {
						id: parseInt(document.getElementById('product-category').value)
					},
					createdAt: createdAt,
					isActive: document.getElementById('product-active').checked
				};
				api.products.update(updatedProduct)
				.then(resp => {
					if (resp.status !== 200) {
						throw new Error('Ошибка при обновлении товара')
					}
					modal.hide();
					filter();
				});
			});
}

function deleteProduct(id) {
	const modal = new bootstrap.Modal(
			document.getElementById('deleteProductModal'));
	modal.show();

	document.getElementById('confirm-delete').onclick = function () {
		api.products.delete(id)
		.then(resp => {
			if (resp.status !== 200) {
				throw new Error('Ошибка при удалении товара')
			}
			modal.hide();
			currentPage = 0;
			filter();
		});
	};
}

function createProduct() {
	api.category.get().then(resp => resp.json()).then(categories => {
		const modalBody = document.getElementById("edit-product-body");
		const today = new Date().toISOString().split('T')[0]; // Текущая дата в формате YYYY-MM-DD

		const categoryOptions = categories.map(category =>
				`<option value="${category.id}">${category.name}</option>`).join('');

		modalBody.innerHTML = `
			<form id="edit-product-form">
				<div class="mb-3">
					<label class="form-label">Название</label>
					<input type="text" class="form-control" id="product-name" required>
				</div>
				<div class="mb-3">
					<label class="form-label">Описание</label>
					<textarea class="form-control" id="product-description"></textarea>
				</div>
				<div class="mb-3">
					<label class="form-label">Цена</label>
					<input type="number" class="form-control" id="product-price" step="0.01" required>
				</div>
				<div class="mb-3">
					<label class="form-label">Ссылка на изображение</label>
					<input type="text" class="form-control" id="product-image" required>
				</div>
				<div class="mb-3">
					<label class="form-label">Категория</label>
					<select class="form-select" id="product-category" required>${categoryOptions}</select>
				</div>
				<div class="mb-3">
					<label class="form-label">Дата создания</label>
					<input type="date" class="form-control" id="product-created" value="${today}">
				</div>
				<div class="mb-3 form-check">
					<input type="checkbox" class="form-check-input" id="product-active" checked>
					<label class="form-check-label" for="product-active">Активен</label>
				</div>
				<div class="d-flex justify-content-end">
					<button type="submit" class="btn btn-success">Создать</button>
				</div>
			</form>
		`;

		const modal = new bootstrap.Modal(
				document.getElementById('editProductModal'));
		document.getElementById(
				'editProductModalLabel').textContent = 'Создать продукт';
		modal.show();

		document.getElementById('edit-product-form').addEventListener('submit',
				(e) => {
					e.preventDefault();

					const createdDate = document.getElementById('product-created').value;
					const createdAt = createdDate ? new Date(createdDate).toISOString()
							: new Date().toISOString();

					const newProduct = {
						name: document.getElementById('product-name').value,
						description: document.getElementById('product-description').value,
						price: parseFloat(document.getElementById('product-price').value),
						imageUrl: document.getElementById('product-image').value,
						category: {
							id: parseInt(document.getElementById('product-category').value)
						},
						createdAt: createdAt,
						isActive: document.getElementById('product-active').checked
					};

					api.products.create(newProduct)
					.then(resp => {
						if (resp.status !== 200 && resp.status !== 201) {
							throw new Error("Ошибка при создании товара");
						}
						modal.hide();
						document.getElementById(
								'editProductModalLabel').textContent = 'Редактировать продукт';
						filter();
					});
				});
	});
}

window.filter = filter;
window.changePage = changePage;
window.editProduct = editProduct;
window.deleteProduct = deleteProduct;
window.createProduct = createProduct;
window.logout = logout;

init();