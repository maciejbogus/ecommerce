package pl.mbogus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.mbogus.productcatalog.HashMapProductStorage;
import pl.mbogus.productcatalog.Product;
import pl.mbogus.productcatalog.ProductCatalog;
import pl.mbogus.sales.*;
import pl.mbogus.sales.cart.CartStorage;
import pl.mbogus.sales.product.ProductCatalogProductDetailsProvider;
import pl.mbogus.sales.product.ProductDetails;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    ProductCatalog createProductCatalog() {
        ProductCatalog productCatalog = new ProductCatalog(new HashMapProductStorage());

        String product1 = productCatalog.addProduct("My ebook", "nice one");
        productCatalog.assignImage(product1, "images/ebook.jpeg");
        productCatalog.changePrice(product1, BigDecimal.valueOf(20.20));
        productCatalog.publishProduct(product1);

        String product2 = productCatalog.addProduct("Other Ebook", "even nicer one");
        productCatalog.assignImage(product2, "images/ebook.jpeg");
        productCatalog.changePrice(product2, BigDecimal.valueOf(30.20));
        productCatalog.publishProduct(product2);

        return productCatalog;
    }


    Sales createSalesViaLambda(ProductCatalog catalog) {
        return new Sales(
                new CartStorage(),
                (String productId) -> {
                    Product product = catalog.loadById(productId);
                    if (product == null) {
                        return Optional.empty();
                    }
                    return Optional.of(new ProductDetails(
                            product.getId(),
                            product.getName(),
                            product.getPrice()));
                }
        );
    }

    @Bean
    Sales createSalesViaObject(ProductCatalog catalog) {
        return new Sales(
                new CartStorage(),
                new ProductCatalogProductDetailsProvider(catalog)
        );
    }
}
