import {handleApiError} from "./errorHandler.js";

const baseUrl = '/api/v1';
const productUrl = `${baseUrl}/products`;
const categoryUrl = `${baseUrl}/categories`;

function getHeaders() {
	return {
		"Content-type": "application/json",
		"Authorization": "Bearer " + localStorage.getItem("accessToken")
	}
}

export const api = {
	products: {
		get: function ({page, name, categoryName, priceFrom, priceTo}) {
			return fetch(
					`${productUrl}?page=${page ? page : 0}`
					+ (name ? `&name=${name}` : '')
					+ (categoryName ? `&categoryName=${categoryName}` : '')
					+ (priceFrom ? `&priceFrom=${priceFrom}` : '')
					+ (priceTo ? `&priceTo=${priceTo}` : ''),
					{headers: getHeaders()}
			).then(handleApiError)
		},
		getById: function (id) {
			return fetch(`${productUrl}/${id}`, {
				headers: getHeaders()
			}).then(handleApiError)
		},
		create: function (product) {
			return fetch(productUrl, {
				method: "POST",
				headers: getHeaders(),
				body: JSON.stringify(product)
			}).then(handleApiError)
		},
		update: function (product) {
			return fetch(productUrl, {
				method: "PUT",
				headers: getHeaders(),
				body: JSON.stringify(product)
			}).then(handleApiError)
		},
		delete: function (id) {
			return fetch(`${productUrl}/${id}`, {
				method: "DELETE",
				headers: getHeaders()
			}).then(handleApiError)
		}
	},
	category: {
		get: function () {
			return fetch(categoryUrl, {
				headers: getHeaders()
			}).then(handleApiError)
		},
		getById: function (id) {
			return fetch(`${categoryUrl}/${id}`, {
				headers: getHeaders()
			}).then(handleApiError)
		},
		create: function (category) {
			return fetch(categoryUrl, {
				method: "POST",
				headers: getHeaders(),
				body: JSON.stringify(category)
			}).then(handleApiError)
		},
		update: function (category) {
			return fetch(categoryUrl, {
				method: "PUT",
				headers: getHeaders(),
				body: JSON.stringify(category)
			}).then(handleApiError)
		},
		delete: function (id) {
			return fetch(`${categoryUrl}/${id}`, {
				method: "DELETE",
				headers: getHeaders()
			}).then(handleApiError)
		}
	}
}