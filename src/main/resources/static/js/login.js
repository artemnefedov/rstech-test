import {handleApiError} from "./errorHandler.js";

function loginRequest() {
	const username = document.getElementById("username").value;
	const password = document.getElementById("password").value

	fetch("/api/v1/auth", {
		method: "POST",
		headers: {"Content-Type": "application/json"},
		body: JSON.stringify({
			"username": username,
			"password": password
		})
	}).then(handleApiError).then(resp => {

		resp.json().then(data => {
			localStorage.setItem("accessToken", data.accessToken);
			localStorage.setItem("refreshToken", data.refreshToken);
			localStorage.setItem("authorities", data.authorities);

			window.location.href = "/";
		});
	});
}

window.loginRequest = loginRequest;