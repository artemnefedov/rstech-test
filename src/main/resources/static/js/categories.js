import {checkAuth, logout} from "./auth.js";
import {api} from "./api.js";
import {Category} from "./models.js";

checkAuth();

const authorities = localStorage.getItem("authorities");
const container = document.getElementById('category-table');

function renderCategoryRow(category) {
	const row = document.createElement('tr');
	row.innerHTML = `
        <td>${category.name}</td>
        <td>${category.description}</td>
    `;

	if (authorities.includes('update') || authorities.includes('delete')) {
		let actionsHtml = `<td class="text-end">
            <div class="d-flex justify-content-end gap-2">`;

		if (authorities.includes('update')) {
			actionsHtml += `<button type="button" class="btn btn-sm btn-outline-primary" onclick="editCategory(${category.id})">Редактировать</button>`;
		}

		if (authorities.includes('delete')) {
			actionsHtml += `<button type="button" class="btn btn-sm btn-outline-danger" onclick="deleteCategory(${category.id})">Удалить</button>`;
		}

		actionsHtml += `</div></td>`;
		row.innerHTML += actionsHtml;
	}

	return row;
}

async function loadCategories() {
	container.innerHTML = '';

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
	})
	.then(categories => {
		categories.forEach(category => {
			container.appendChild(renderCategoryRow(category));
		});
		return categories;
	});
}

async function initCategories() {
	if (authorities.includes('update') || authorities.includes('delete')) {
		document.getElementById(
				'category-table-header').innerHTML += `<th class="text-end">Действия</th>`;
	}

	await loadCategories();
}

function editCategory(id) {
	console.log(id);
	api.category.getById(id)
	.then(resp => resp.json())
	.then(category => {
		const modalBody = document.getElementById("edit-category-body");

		modalBody.innerHTML = `
                <form id="edit-category-form">
                    <div class="mb-3">
                        <label for="category-name" class="form-label">Название</label>
                        <input type="text" class="form-control" id="category-name" value="${category.name}">
                    </div>
                    <div class="mb-3">
                        <label for="category-description" class="form-label">Описание</label>
                        <textarea class="form-control" id="category-description">${category.description}</textarea>
                    </div>
                    <div class="d-flex justify-content-end">
                        <button type="submit" class="btn btn-primary">Сохранить</button>
                    </div>
                </form>
            `;

		const modal = new bootstrap.Modal(
				document.getElementById('editCategoryModal'));
		modal.show();

		document.getElementById('edit-category-form').addEventListener('submit',
				(e) => {
					e.preventDefault();

					const updatedCategory = {
						id: category.id,
						name: document.getElementById('category-name').value,
						description: document.getElementById('category-description').value
					};

					api.category.update(updatedCategory)
					.then(resp => {
						if (resp.status !== 200) {
							throw new Error('Ошибка при обновлении категории')
						}
						modal.hide();
						loadCategories();
					});
				});
	});
}

function deleteCategory(id) {
	const modal = new bootstrap.Modal(
			document.getElementById('deleteCategoryModal'));
	modal.show();

	document.getElementById('confirm-delete').onclick = function () {
		api.category.delete(id)
		.then(resp => {
			if (resp.status !== 200) {
				throw new Error('Ошибка при удалении категории')
			}
			modal.hide();
			loadCategories();
		});
	};
}

function createCategory() {
	const modalBody = document.getElementById("edit-category-body");

	modalBody.innerHTML = `
        <form id="edit-category-form">
            <div class="mb-3">
                <label class="form-label">Название</label>
                <input type="text" class="form-control" id="category-name">
            </div>
            <div class="mb-3">
                <label class="form-label">Описание</label>
                <textarea class="form-control" id="category-description"></textarea>
            </div>
            <div class="d-flex justify-content-end">
                <button type="submit" class="btn btn-success">Создать</button>
            </div>
        </form>
    `;

	const modal = new bootstrap.Modal(
			document.getElementById('editCategoryModal'));
	modal.show();

	document.getElementById('edit-category-form').addEventListener('submit',
			(e) => {
				e.preventDefault();

				const newCategory = {
					name: document.getElementById('category-name').value,
					description: document.getElementById('category-description').value
				};

				api.category.create(newCategory)
				.then(resp => {
					if (resp.status !== 200 && resp.status !== 201) {
						throw new Error("Ошибка при создании категории");
					}
					modal.hide();
					loadCategories();
				});
			});
}

window.editCategory = editCategory;
window.deleteCategory = deleteCategory;
window.createCategory = createCategory;
window.logout = logout;

initCategories();