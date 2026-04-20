package com.deliverytech.delivery_api.config;

import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.deliverytech.delivery_api.enums.OrderStatus;
import com.deliverytech.delivery_api.enums.RestaurantCategory;
import com.deliverytech.delivery_api.enums.UserRole;
import com.deliverytech.delivery_api.model.Client;
import com.deliverytech.delivery_api.model.ItemOrdered;
import com.deliverytech.delivery_api.model.Order;
import com.deliverytech.delivery_api.model.Product;
import com.deliverytech.delivery_api.model.Restaurant;
import com.deliverytech.delivery_api.model.User;
import com.deliverytech.delivery_api.repository.ClientRepository;
import com.deliverytech.delivery_api.repository.OrderRepository;
import com.deliverytech.delivery_api.repository.ProductRepository;
import com.deliverytech.delivery_api.repository.RestaurantRepository;
import com.deliverytech.delivery_api.repository.UserRepository;
import com.deliverytech.delivery_api.repository.ItemOrderedRepository;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initiateData(
        ClientRepository clientRepository, 
        RestaurantRepository restaurantRepository,
        ProductRepository productRepository,
        OrderRepository orderRepository,
        ItemOrderedRepository itemOrderedRepository, 
        UserRepository userRepository
        ) {
            return args -> {
                String[] names = {"João Silva", "Maria Costa", "Fernando Carvalho"};
                String[] emails = {"joaosilva@exemplo.com", "mariacosta@exemplo.com", "fernandocarvalho@exemplo.com"};
                String[] phones = {"(12)91234-1234", "(12)91234-5678", "(12)91234-9999"};
                String[] addresses = {"Av 1, 123", "Av 2, 123", "Av 3, 123"};

                String[] restaurantNames = {"Big's Hamburgueria", "Pizzaria Donatelo"};
                String[] restaurantEmails = {"bigshamburgueria@exemplo.com", "pizzariadonatelo@exemplo.com"};
                RestaurantCategory[] categories = {RestaurantCategory.HAMBURGUERIA, RestaurantCategory.PIZZARIA};
                String[] restaurantPhones = {"1234-1234", "5678-5678"};
                String[] restaurantAddresses = {"Av 11, 123", "Av 22, 123"};
                BigDecimal[] ratings = {new BigDecimal("4.1"), new BigDecimal("3.8")};

                BigDecimal deliveryFee = new BigDecimal("5.00");
                
                String[] productNames = {"Pizza de Queijo", "Pizza de Frango", "Big's Burger", "Hamburguer de Frango"};
                String[] productDescription = {"Pizza de Queijo ao forno", "Pizza de Frango com catupiry", "Hamburguer a moda da casa", "Hamburger de frango e molho especial"};
                String[] productCategories = {"Pizza", "Pizza", "Hamburger", "Hamburger"};
                BigDecimal[] productPrice = {new BigDecimal("40.50"), new BigDecimal("50.00"), new BigDecimal("59.99"), new BigDecimal("39.99")};



                System.out.println("========= Inserindo Clientes =========");
                
                List<Client> clients = new ArrayList<>();

                for (int i = 0; i < names.length; i++) {

                    User user = new User();
                    user.setEmail(emails[i]);
                    user.setPassword("123456");
                    user.setRole(UserRole.CLIENT);
                    user.setActive(true);

                    userRepository.save(user);

                    Client client = new Client();
                    client.setName(names[i]);
                    client.setEmail(emails[i]);
                    client.setPhone(phones[i]);
                    client.setAddress(addresses[i]);
                    client.setActive(true);

                    client.setUser(user);
                    clients.add(client);
                }

                clientRepository.saveAll(clients);

                System.out.println("========= Consultando Clientes =========");
                System.out.println("=> Busca por EMAIL: ");
                clientRepository.findByEmail(emails[1]).ifPresent(System.out::println);
                
                String nameContent = names[1].substring(0, 2);
                System.out.println("=> Busca por cliente contendo " + nameContent + ": ");
                clientRepository.findByNameContainingIgnoreCase(nameContent).forEach(c -> System.out.println(c.getName()));
                
                System.out.println("=> Verificar se EMAIL existe: ");
                Boolean exists = clientRepository.existsByEmail(emails[2]);
                System.out.println("Existe Fernando? " + exists);

                Pageable pageable = PageRequest.of(0,10);
                System.out.println("=> Cliente ativos: ");
                clientRepository.findByActiveTrue(pageable).forEach(c -> System.out.println(c.getName()));

                System.out.println("========= Inserindo Restaurantes =========");            

                List<Restaurant> restaurants = new ArrayList<>();
                for (int i = 0; i < restaurantNames.length; i++) {
                    Restaurant restaurant = new Restaurant();
                    User user = new User();

                    user.setEmail(restaurantEmails[i]);
                    user.setPassword("123456");
                    user.setRole(UserRole.RESTAURANT);
                    user.setActive(true);
                    
                    userRepository.save(user);

                    restaurant.setName(restaurantNames[i]);
                    restaurant.setCategory(categories[i]);
                    restaurant.setPhone(restaurantPhones[i]);
                    restaurant.setAddress(restaurantAddresses[i]);
                    restaurant.setRating(ratings[i]);
                    restaurant.setActive(true);

                    restaurant.setUser(user);
                    restaurants.add(restaurant);
                }

                restaurantRepository.saveAll(restaurants);

                System.out.println("========= Consultando Restaurantes =========");
                System.out.println("=> Busca por Categoria: ");
                restaurantRepository.findByCategory(categories[1]).forEach(c -> System.out.println(c.getName()));

                System.out.println("=> Restaurantes ativos: ");
                restaurantRepository.findByActiveTrue().forEach(c -> System.out.println(c.getName()));

                System.out.println("========= Inserindo Produtos =========");
                
                List<Product> products = new ArrayList<>();
                for (int i = 0; i < productNames.length; i++) {
                    Product product = new Product();
                    product.setName(productNames[i]);
                    product.setDescription(productDescription[i]);
                    product.setCategory(productCategories[i]);
                    product.setPrice(productPrice[i]);
                    product.setAvailable(true);
                    if (i < 2) {
                        product.setRestaurant(restaurants.get(0));
                    } else {
                        product.setRestaurant(restaurants.get(1));
                    }
                    products.add(product);
                }

                productRepository.saveAll(products);

                System.out.println("========= Inserindo Pedidos =========");
                
                List<Order> orders = new ArrayList<>();
                for (int i = 0; i < names.length; i++) {
                    Order order = new Order();
                    order.setDeliveryAddress(addresses[i]);
                    order.setStatus(OrderStatus.PENDING);
                    order.setDeliveryFee(deliveryFee);
                    order.setTotalPrice(new BigDecimal("0.00"));
                    order.setClient(clients.get(i));
                    // Usa modulo para garantir índice válido quando há menos restaurantes que pedidos
                    order.setRestaurant(restaurants.get(i % restaurants.size()));
                    orders.add(order);
                }
                
                orderRepository.saveAll(orders);

                System.out.println("========= Inserindo Items Pedidos =========");

                Random random = new Random();
                List<ItemOrdered> itemsOrdered = new ArrayList<>();
                for (int i = 0; i < orders.size(); i++) {
                    int numItems = random.nextInt(4) + 1;
                    for (int j = 0; j < numItems; j++) {
                        ItemOrdered itemOrdered = new ItemOrdered();
                        Product product = products.get(random.nextInt(products.size()));
                        int quantity = random.nextInt(5) + 1;
                        itemOrdered.setProduct(product);
                        itemOrdered.setUnitPrice(product.getPrice());
                        itemOrdered.setQuantity(quantity);
                        itemOrdered.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
                        itemOrdered.setOrder(orders.get(i));
                        itemsOrdered.add(itemOrdered);
                    }
                }
                
                itemOrderedRepository.saveAll(itemsOrdered);

                // Calcula total de cada pedido com base nos itens para refletir vendas reais
                orders.forEach(order -> {
                    BigDecimal total = itemsOrdered.stream()
                        .filter(item -> item.getOrder().getId().equals(order.getId()))
                        .map(ItemOrdered::getSubtotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    order.setTotalPrice(total);
                });
                orderRepository.saveAll(orders);

                System.out.println("========= DTO - Items do Pedido =========");
                for (int i = 0; i < orders.size(); i++) {
                    itemOrderedRepository.searchItemsWithProducts(orders.get(i).getId()).forEach(it -> System.out.println(
                        "Produto: " + it.getProductName() +
                        " | Quantidade: " + it.getQuantity() +
                        " | Subtotal: " + it.getSubtotal()
                    ));
                }

                System.out.println("========= DTO - Vendas por Restaurante =========");
                orderRepository.searchSalesPerRestaurant()
                .forEach(r -> System.out.println(
                    r.getRestaurantName() + " - " + r.getTotalSales()
                ));
            };
    }
}
