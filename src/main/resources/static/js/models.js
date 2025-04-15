export class Category {
	constructor(id, name, description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
}

export class Product {
	constructor(id, name, description, price, imageUrl, category, createdAt,
			isActive) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imageUrl = imageUrl;
		this.category = category;
		this.createdAt = createdAt;
		this.isActive = isActive;
	}
}