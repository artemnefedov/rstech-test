export function checkAuth() {
	if (localStorage.getItem("accessToken")) {
		fetch("/api/v1/auth/validate", {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
				"Authorization": "Bearer " + localStorage.getItem("accessToken")
			},
		}).then(resp => {
			if (resp.status === 200) {
				resp.json().then(data => {
					if (!data) {
						fetch("/api/v1/auth/refresh", {
							method: "POST",
							headers: {
								"Content-Type": "application/json",
								"Authorization": "Bearer " + localStorage.getItem(
										"refreshToken")
							},
						}).then(resp => {
							if (resp.status === 200) {
								resp.json().then(data => {
									localStorage.setItem("accessToken", data.accessToken);
									localStorage.setItem("refreshToken", data.refreshToken);
									localStorage.setItem("authorities", data.authorities);
								});
							} else {
								clearTokensAndRedirect();
							}
						});
					}
				});
			} else {
				clearTokensAndRedirect();
			}
		})
	} else {
		clearTokensAndRedirect();
	}
}

function clearTokensAndRedirect() {
	localStorage.removeItem("accessToken");
	localStorage.removeItem("refreshToken");
	localStorage.removeItem("authorities");
	window.location.href = "/login";
}

export function logout() {
	clearTokensAndRedirect();
}