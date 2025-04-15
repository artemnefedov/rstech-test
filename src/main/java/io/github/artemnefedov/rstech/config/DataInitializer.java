package io.github.artemnefedov.rstech.config;

import io.github.artemnefedov.rstech.entity.Category;
import io.github.artemnefedov.rstech.entity.Product;
import io.github.artemnefedov.rstech.repository.CategoryRepository;
import io.github.artemnefedov.rstech.repository.ProductRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final Random random = new Random();

    public DataInitializer(CategoryRepository categoryRepository,
            ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0 || productRepository.count() > 0) {
            return;
        }

        Category electronics = new Category();
        electronics.setName("Electronics");
        electronics.setDescription("Devices, gadgets, and accessories for modern life.");
        categoryRepository.save(electronics);

        Category homeAppliances = new Category();
        homeAppliances.setName("Home Appliances");
        homeAppliances.setDescription("Appliances to make your home smarter and more efficient.");
        categoryRepository.save(homeAppliances);

        Category furniture = new Category();
        furniture.setName("Furniture");
        furniture.setDescription("Stylish and comfortable furniture for home and office.");
        categoryRepository.save(furniture);

        Category toys = new Category();
        toys.setName("Toys");
        toys.setDescription("Toys for kids of all ages.");
        categoryRepository.save(toys);

        Category deleted = new Category();
        deleted.setName("Не определено");
        deleted.setDescription("Категория удалена");
        categoryRepository.save(deleted);

        List<String> electronicsNames = List.of(
                "Smartphone",
                "Laptop",
                "Tablet",
                "Smart Watch",
                "Headphones",
                "Bluetooth Speaker",
                "Camera",
                "Drone",
                "Smart TV",
                "Gaming Console"
        );

        List<String> homeApplianceNames = List.of(
                "Air Conditioner",
                "Washing Machine",
                "Refrigerator",
                "Microwave Oven",
                "Vacuum Cleaner",
                "Dishwasher",
                "Coffee Maker",
                "Iron",
                "Blender",
                "Electric Kettle"
        );

        List<String> furnitureNames = List.of(
                "Sofa",
                "Chair",
                "Table",
                "Bed",
                "Bookshelf",
                "Wardrobe",
                "Dining Set",
                "Desk",
                "Armchair",
                "Cupboard"
        );

        List<String> toyNames = List.of(
                "Lego Set",
                "Toy Car",
                "Doll",
                "Action Figure",
                "Puzzle",
                "Board Game",
                "Teddy Bear",
                "Building Blocks",
                "Train Set",
                "Drone"
        );

        generateProductsForCategory(electronics, electronicsNames, 40);
        generateProductsForCategory(homeAppliances, homeApplianceNames, 20);
        generateProductsForCategory(furniture, furnitureNames, 15);
        generateProductsForCategory(toys, toyNames, 10);
    }

    private void generateProductsForCategory(Category category, List<String> productNames,
            int count) {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Product product = new Product();
            product.setName(productNames.get(random.nextInt(productNames.size())));
            product.setDescription("High-quality " + product.getName() + " for your needs.");
            product.setPrice(BigDecimal.valueOf(50 + random.nextDouble() * 950));
            product.setImageUrl(
                    "https://picsum.photos/id/" + random.nextInt(100) + "/260/200.webp");
            product.setCategory(category);
            product.setCreatedAt(LocalDate.now());
            product.setActive(random.nextBoolean());

            products.add(product);
        }
        productRepository.saveAll(products);
    }
}
