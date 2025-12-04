Ownership & Editing Notice

This file is maintained by Karim Lahyani, Ahmad Kosaele, and Mohamed Khaled Becetti.
Do not edit or modify this file without prior permission from the responsible team.

You may import and use the interfaces provided by this package, but direct modifications to this file are strictly restricted.public class RestaurantData implements FOS_DATA.IRestaurantData {
    public boolean saveRestaurantInfo(Restaurant restaurant) {
        String sql = "INSERT INTO Restaurant (manager_id, name, cuisine_type, city) VALUES (?, ?, ?, ?)";

        try {
            Connection connection = DatabaseConnection.getConnection();

            boolean var16;
            label111: {
                try {
                    PreparedStatement statement;
                    label103: {
                        statement = connection.prepareStatement("INSERT INTO Restaurant (manager_id, name, cuisine_type, city) VALUES (?, ?, ?, ?)", 1);

                        try {
                            statement.setInt(1, restaurant.getManagerID().getUserID());
                            statement.setString(2, restaurant.getRestaurantName());
                            statement.setString(3, restaurant.getCuisineType());
                            statement.setString(4, restaurant.getCity());
                            int rowsAffected = statement.executeUpdate();
                            if (rowsAffected <= 0) {
                                break label103;
                            }

                            ResultSet generatedKeys = statement.getGeneratedKeys();

                            try {
                                if (generatedKeys.next()) {
                                    restaurant.setRestaurantID(generatedKeys.getInt(1));
                                }
                            } catch (Throwable var12) {
                                if (generatedKeys != null) {
                                    try {
                                        generatedKeys.close();
                                    } catch (Throwable var11) {
                                        var12.addSuppressed(var11);
                                    }
                                }

                                throw var12;
                            }

                            if (generatedKeys != null) {
                                generatedKeys.close();
                            }

                            var16 = true;
                        } catch (Throwable var13) {
                            if (statement != null) {
                                try {
                                    statement.close();
                                } catch (Throwable var10) {
                                    var13.addSuppressed(var10);
                                }
                            }

                            throw var13;
                        }

                        if (statement != null) {
                            statement.close();
                        }
                        break label111;
                    }

                    if (statement != null) {
                        statement.close();
                    }
                } catch (Throwable var14) {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (Throwable var9) {
                            var14.addSuppressed(var9);
                        }
                    }

                    throw var14;
                }

                if (connection != null) {
                    connection.close();
                }

                return false;
            }

            if (connection != null) {
                connection.close();
            }

            return var16;
        } catch (SQLException var15) {
            System.out.println("Database failed to save restaurant: " + var15.getMessage());
            return false;
        }
    }

    public ArrayList<Restaurant> getManagerRestaurants(Manager manager) {
        int managerId = manager.getUserID();
        ArrayList<Restaurant> restaurants = new ArrayList();
        String sql = "SELECT restaurant_id, name, cuisine_type, city FROM Restaurant WHERE manager_id = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();

            try {
                PreparedStatement statement = connection.prepareStatement("SELECT restaurant_id, name, cuisine_type, city FROM Restaurant WHERE manager_id = ?");

                try {
                    statement.setInt(1, managerId);
                    ResultSet resultSet = statement.executeQuery();

                    try {
                        while(resultSet.next()) {
                            int restaurantId = resultSet.getInt("restaurant_id");
                            String name = resultSet.getString("name");
                            String cuisineType = resultSet.getString("cuisine_type");
                            String city = resultSet.getString("city");
                            Restaurant restaurant = new Restaurant(restaurantId, name, cuisineType, city);
                            restaurant.setManagerID(manager);
                            restaurant.setMenu(this.getRestaurantMenuItems(restaurant));
                            restaurants.add(restaurant);
                        }
                    } catch (Throwable var16) {
                        if (resultSet != null) {
                            try {
                                resultSet.close();
                            } catch (Throwable var15) {
                                var16.addSuppressed(var15);
                            }
                        }

                        throw var16;
                    }

                    if (resultSet != null) {
                        resultSet.close();
                    }
                } catch (Throwable var17) {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (Throwable var14) {
                            var17.addSuppressed(var14);
                        }
                    }

                    throw var17;
                }

                if (statement != null) {
                    statement.close();
                }
            } catch (Throwable var18) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable var13) {
                        var18.addSuppressed(var13);
                    }
                }

                throw var18;
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException var19) {
            System.out.println("Database failed to fetch manager restaurants: " + var19.getMessage());
        }

        return restaurants;
    }

    public ArrayList<Restaurant> getRestaurantByCity(String city) {
        ArrayList<Restaurant> restaurants = new ArrayList();
        String sql = "SELECT restaurant_id, name, cuisine_type, city, manager_id FROM Restaurant WHERE city = ?";

        try {
            Connection connection = DatabaseConnection.getConnection();

            try {
                PreparedStatement statement = connection.prepareStatement("SELECT restaurant_id, name, cuisine_type, city, manager_id FROM Restaurant WHERE city = ?");

                try {
                    statement.setString(1, city);
                    ResultSet resultSet = statement.executeQuery();

                    try {
                        while(resultSet.next()) {
                            int restaurantId = resultSet.getInt("restaurant_id");
                            String name = resultSet.getString("name");
                            String cuisineType = resultSet.getString("cuisine_type");
                            String restaurantCity = resultSet.getString("city");
                            Restaurant restaurant = new Restaurant(restaurantId, name, cuisineType, restaurantCity);
                            restaurant.setMenu(this.getRestaurantMenuItems(restaurant));
                            restaurants.add(restaurant);
                        }
                    } catch (Throwable var15) {
                        if (resultSet != null) {
                            try {
                                resultSet.close();
                            } catch (Throwable var14) {
                                var15.addSuppressed(var14);
                            }
                        }

                        throw var15;
                    }

                    if (resultSet != null) {
                        resultSet.close();
                    }
                } catch (Throwable var16) {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (Throwable var13) {
                            var16.addSuppressed(var13);
                        }
                    }

                    throw var16;
                }

                if (statement != null) {
                    statement.close();
                }
            } catch (Throwable var17) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable var12) {
                        var17.addSuppressed(var12);
                    }
                }

                throw var17;
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException var18) {
            System.out.println("Database failed to fetch restaurants by city: " + var18.getMessage());
        }

        return restaurants;
    }

    public boolean addMenuItem(MenuItem menuItem, Restaurant restaurant) {
        int restaurantId = restaurant.getRestaurantID();
        String sql = "INSERT INTO MenuItem (restaurant_id, item_name, description, price) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, restaurantId);
            statement.setString(2, menuItem.getItemName());
            statement.setString(3, menuItem.getDescription());
            statement.setDouble(4, menuItem.getPrice());
            int rowsAffected = statement.executeUpdate();
            sql = "SELECT LAST_INSERT_ID() AS menu_item_id";
            ResultSet resultSet = statement.executeQuery(sql);
            menuItem.setMenuItemID(resultSet.getInt("menu_item_id"));
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to add menu Item to restaurant: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMenuItem(MenuItem menuItem) {
        final String sql = "UPDATE MenuItem SET item_name = ?, description = ?, price = ? WHERE menu_item_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, menuItem.getItemName());
            statement.setString(2, menuItem.getDescription());
            statement.setDouble(3, menuItem.getPrice());
            statement.setInt(4, menuItem.getMenuItemID());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to update menu Item: " + e.getMessage());
            return false;
        }
    }

    public boolean removeMenuItem(MenuItem menuItem) {
        final String sql = "DELETE FROM MenuItem WHERE menu_item_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, menuItem.getMenuItemID());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Database failed to remove menu Item: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<MenuItem> getRestaurantMenuItems(Restaurant restaurant) {
        int restaurantId = restaurant.getRestaurantID();
        ArrayList<MenuItem> menuItems = new ArrayList();
        final String sql = "SELECT menu_item_id, item_name, description, price FROM MenuItem WHERE restaurant_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, restaurantId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int menuItemId = resultSet.getInt("menu_item_id");
                String itemName = resultSet.getString("item_name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                MenuItem menuItem = new MenuItem(menuItemId, itemName, description, price);
                menuItems.add(menuItem);
            }
            return menuItems;
        } catch (SQLException e) {
            System.out.println("Database failed to fetch restaurant menu items: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<Order> fetchRestaurantOrders(Restaurant restaurant) {
        int restaurantId = restaurant.getRestaurantID();
        ArrayList<Order> orders = new ArrayList<>();
        String sql = "SELECT o.order_id, o.order_date, o.status, r.restaurant_id as restaurant_id, \n" +
                "                       a.address_id,\n" +
                "                       rt.rating, rt.comment\n" +
                "                FROM Order o\n" +
                "                JOIN Restaurant r ON o.restaurant_id = r.restaurant_id\n" +
                "                JOIN Address a ON o.delivery_address_id = a.address_id\n" +
                "                LEFT JOIN Rating rt ON o.order_id = rt.cart_id\n" +
                "                WHERE o.restaurant_id = ?\n" +
                "                GROUP BY o.order_id, o.order_date, o.status, r.name, rt.rating, rt.comment\n" +
                "                ORDER BY o.order_date DESC";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int order_id = resultSet.getInt("order_id");
                    Date date = resultSet.getDate("order_date");
                    OrderStatus status = OrderStatus.valueOf(resultSet.getString("status"));
                    Rating rating = new Rating(resultSet.getObject("rating") != null ? resultSet.getInt("rating") : null, resultSet.getString("comment"));
                    int addressId = resultSet.getInt("address_id");
                    Address deliveryAddress = new Address();
                    for (Address address : customer.getAddresses()) {
                        if (address.getAddressID() == addressId) {
                            deliveryAddress = address;
                            break;
                        }
                    }
                    String restaurantName = "";
                    ArrayList<CartItem> items = new ArrayList<>();
                    sql = "SELECT mi.menu_item_id, oi.quantity" +
                            "FROM OrderItem oi " +
                            "JOIN MenuItem mi ON oi.menu_item_id = mi.menu_item_id " +
                            "WHERE oi.order_id = ?";
                    try (PreparedStatement itemStatement = connection.prepareStatement(sql)) {
                        itemStatement.setInt(1, order_id);
                        try (ResultSet itemResultSet = itemStatement.executeQuery()) {
                            int menuItemId = itemResultSet.getInt("menu_item_id");
                            for (Restaurant restaurant : restaurants) {
                                if (restaurant.getRestaurantID() == restaurantId) {
                                    restaurantName = restaurant.getRestaurantName();
                                    for (MenuItem menuItem : restaurant.getMenu()) {
                                        if (menuItem.getMenuItemID() == menuItemId) {
                                            int quantity = itemResultSet.getInt("quantity");
                                            items.add(new CartItem(menuItem, quantity));
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        orders.add(new Order(deliveryAddress.toString(), items, date, status, restaurantName, order_id, rating));
                    }
                }
            } catch (SQLException e) {
                System.out.println("Database failed to fetch customer orders");
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    }public class RestaurantData implements FOS_DATA.IRestaurantData {
        public boolean saveRestaurantInfo(Restaurant restaurant) {
            String sql = "INSERT INTO Restaurant (manager_id, name, cuisine_type, city) VALUES (?, ?, ?, ?)";
    
            try {
                Connection connection = DatabaseConnection.getConnection();
    
                boolean var16;
                label111: {
                    try {
                        PreparedStatement statement;
                        label103: {
                            statement = connection.prepareStatement("INSERT INTO Restaurant (manager_id, name, cuisine_type, city) VALUES (?, ?, ?, ?)", 1);
    
                            try {
                                statement.setInt(1, restaurant.getManagerID().getUserID());
                                statement.setString(2, restaurant.getRestaurantName());
                                statement.setString(3, restaurant.getCuisineType());
                                statement.setString(4, restaurant.getCity());
                                int rowsAffected = statement.executeUpdate();
                                if (rowsAffected <= 0) {
                                    break label103;
                                }
    
                                ResultSet generatedKeys = statement.getGeneratedKeys();
    
                                try {
                                    if (generatedKeys.next()) {
                                        restaurant.setRestaurantID(generatedKeys.getInt(1));
                                    }
                                } catch (Throwable var12) {
                                    if (generatedKeys != null) {
                                        try {
                                            generatedKeys.close();
                                        } catch (Throwable var11) {
                                            var12.addSuppressed(var11);
                                        }
                                    }
    
                                    throw var12;
                                }
    
                                if (generatedKeys != null) {
                                    generatedKeys.close();
                                }
    
                                var16 = true;
                            } catch (Throwable var13) {
                                if (statement != null) {
                                    try {
                                        statement.close();
                                    } catch (Throwable var10) {
                                        var13.addSuppressed(var10);
                                    }
                                }
    
                                throw var13;
                            }
    
                            if (statement != null) {
                                statement.close();
                            }
                            break label111;
                        }
    
                        if (statement != null) {
                            statement.close();
                        }
                    } catch (Throwable var14) {
                        if (connection != null) {
                            try {
                                connection.close();
                            } catch (Throwable var9) {
                                var14.addSuppressed(var9);
                            }
                        }
    
                        throw var14;
                    }
    
                    if (connection != null) {
                        connection.close();
                    }
    
                    return false;
                }
    
                if (connection != null) {
                    connection.close();
                }
    
                return var16;
            } catch (SQLException var15) {
                System.out.println("Database failed to save restaurant: " + var15.getMessage());
                return false;
            }
        }
    
        public ArrayList<Restaurant> getManagerRestaurants(Manager manager) {
            int managerId = manager.getUserID();
            ArrayList<Restaurant> restaurants = new ArrayList();
            String sql = "SELECT restaurant_id, name, cuisine_type, city FROM Restaurant WHERE manager_id = ?";
    
            try {
                Connection connection = DatabaseConnection.getConnection();
    
                try {
                    PreparedStatement statement = connection.prepareStatement("SELECT restaurant_id, name, cuisine_type, city FROM Restaurant WHERE manager_id = ?");
    
                    try {
                        statement.setInt(1, managerId);
                        ResultSet resultSet = statement.executeQuery();
    
                        try {
                            while(resultSet.next()) {
                                int restaurantId = resultSet.getInt("restaurant_id");
                                String name = resultSet.getString("name");
                                String cuisineType = resultSet.getString("cuisine_type");
                                String city = resultSet.getString("city");
                                Restaurant restaurant = new Restaurant(restaurantId, name, cuisineType, city);
                                restaurant.setManagerID(manager);
                                restaurant.setMenu(this.getRestaurantMenuItems(restaurant));
                                restaurants.add(restaurant);
                            }
                        } catch (Throwable var16) {
                            if (resultSet != null) {
                                try {
                                    resultSet.close();
                                } catch (Throwable var15) {
                                    var16.addSuppressed(var15);
                                }
                            }
    
                            throw var16;
                        }
    
                        if (resultSet != null) {
                            resultSet.close();
                        }
                    } catch (Throwable var17) {
                        if (statement != null) {
                            try {
                                statement.close();
                            } catch (Throwable var14) {
                                var17.addSuppressed(var14);
                            }
                        }
    
                        throw var17;
                    }
    
                    if (statement != null) {
                        statement.close();
                    }
                } catch (Throwable var18) {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (Throwable var13) {
                            var18.addSuppressed(var13);
                        }
                    }
    
                    throw var18;
                }
    
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException var19) {
                System.out.println("Database failed to fetch manager restaurants: " + var19.getMessage());
            }
    
            return restaurants;
        }
    
        public ArrayList<Restaurant> getRestaurantByCity(String city) {
            ArrayList<Restaurant> restaurants = new ArrayList();
            String sql = "SELECT restaurant_id, name, cuisine_type, city, manager_id FROM Restaurant WHERE city = ?";
    
            try {
                Connection connection = DatabaseConnection.getConnection();
    
                try {
                    PreparedStatement statement = connection.prepareStatement("SELECT restaurant_id, name, cuisine_type, city, manager_id FROM Restaurant WHERE city = ?");
    
                    try {
                        statement.setString(1, city);
                        ResultSet resultSet = statement.executeQuery();
    
                        try {
                            while(resultSet.next()) {
                                int restaurantId = resultSet.getInt("restaurant_id");
                                String name = resultSet.getString("name");
                                String cuisineType = resultSet.getString("cuisine_type");
                                String restaurantCity = resultSet.getString("city");
                                Restaurant restaurant = new Restaurant(restaurantId, name, cuisineType, restaurantCity);
                                restaurant.setMenu(this.getRestaurantMenuItems(restaurant));
                                restaurants.add(restaurant);
                            }
                        } catch (Throwable var15) {
                            if (resultSet != null) {
                                try {
                                    resultSet.close();
                                } catch (Throwable var14) {
                                    var15.addSuppressed(var14);
                                }
                            }
    
                            throw var15;
                        }
    
                        if (resultSet != null) {
                            resultSet.close();
                        }
                    } catch (Throwable var16) {
                        if (statement != null) {
                            try {
                                statement.close();
                            } catch (Throwable var13) {
                                var16.addSuppressed(var13);
                            }
                        }
    
                        throw var16;
                    }
    
                    if (statement != null) {
                        statement.close();
                    }
                } catch (Throwable var17) {
                    if (connection != null) {
                        try {
                            connection.close();
                        } catch (Throwable var12) {
                            var17.addSuppressed(var12);
                        }
                    }
    
                    throw var17;
                }
    
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException var18) {
                System.out.println("Database failed to fetch restaurants by city: " + var18.getMessage());
            }
    
            return restaurants;
        }
    
        public boolean addMenuItem(MenuItem menuItem, Restaurant restaurant) {
            int restaurantId = restaurant.getRestaurantID();
            String sql = "INSERT INTO MenuItem (restaurant_id, item_name, description, price) VALUES (?, ?, ?, ?)";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, restaurantId);
                statement.setString(2, menuItem.getItemName());
                statement.setString(3, menuItem.getDescription());
                statement.setDouble(4, menuItem.getPrice());
                int rowsAffected = statement.executeUpdate();
                sql = "SELECT LAST_INSERT_ID() AS menu_item_id";
                ResultSet resultSet = statement.executeQuery(sql);
                menuItem.setMenuItemID(resultSet.getInt("menu_item_id"));
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.out.println("Database failed to add menu Item to restaurant: " + e.getMessage());
                return false;
            }
        }
    
        public boolean updateMenuItem(MenuItem menuItem) {
            final String sql = "UPDATE MenuItem SET item_name = ?, description = ?, price = ? WHERE menu_item_id = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, menuItem.getItemName());
                statement.setString(2, menuItem.getDescription());
                statement.setDouble(3, menuItem.getPrice());
                statement.setInt(4, menuItem.getMenuItemID());
                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.out.println("Database failed to update menu Item: " + e.getMessage());
                return false;
            }
        }
    
        public boolean removeMenuItem(MenuItem menuItem) {
            final String sql = "DELETE FROM MenuItem WHERE menu_item_id = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, menuItem.getMenuItemID());
                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.out.println("Database failed to remove menu Item: " + e.getMessage());
                return false;
            }
        }
    
        public ArrayList<MenuItem> getRestaurantMenuItems(Restaurant restaurant) {
            int restaurantId = restaurant.getRestaurantID();
            ArrayList<MenuItem> menuItems = new ArrayList();
            final String sql = "SELECT menu_item_id, item_name, description, price FROM MenuItem WHERE restaurant_id = ?";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, restaurantId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    int menuItemId = resultSet.getInt("menu_item_id");
                    String itemName = resultSet.getString("item_name");
                    String description = resultSet.getString("description");
                    double price = resultSet.getDouble("price");
                    MenuItem menuItem = new MenuItem(menuItemId, itemName, description, price);
                    menuItems.add(menuItem);
                }
                return menuItems;
            } catch (SQLException e) {
                System.out.println("Database failed to fetch restaurant menu items: " + e.getMessage());
            }
            return null;
        }
    
        public ArrayList<Order> fetchRestaurantOrders(Restaurant restaurant) {
            int restaurantId = restaurant.getRestaurantID();
            ArrayList<Order> orders = new ArrayList<>();
            String sql = "SELECT o.order_id, o.order_date, o.status, r.restaurant_id as restaurant_id, \n" +
                    "                       a.address_id,\n" +
                    "                       rt.rating, rt.comment\n" +
                    "                FROM Order o\n" +
                    "                JOIN Restaurant r ON o.restaurant_id = r.restaurant_id\n" +
                    "                JOIN Address a ON o.delivery_address_id = a.address_id\n" +
                    "                LEFT JOIN Rating rt ON o.order_id = rt.cart_id\n" +
                    "                WHERE o.restaurant_id = ?\n" +
                    "                GROUP BY o.order_id, o.order_date, o.status, r.name, rt.rating, rt.comment\n" +
                    "                ORDER BY o.order_date DESC";
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, customerId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int order_id = resultSet.getInt("order_id");
                        Date date = resultSet.getDate("order_date");
                        OrderStatus status = OrderStatus.valueOf(resultSet.getString("status"));
                        Rating rating = new Rating(resultSet.getObject("rating") != null ? resultSet.getInt("rating") : null, resultSet.getString("comment"));
                        int addressId = resultSet.getInt("address_id");
                        Address deliveryAddress = new Address();
                        for (Address address : customer.getAddresses()) {
                            if (address.getAddressID() == addressId) {
                                deliveryAddress = address;
                                break;
                            }
                        }
                        String restaurantName = "";
                        ArrayList<CartItem> items = new ArrayList<>();
                        sql = "SELECT mi.menu_item_id, oi.quantity" +
                                "FROM OrderItem oi " +
                                "JOIN MenuItem mi ON oi.menu_item_id = mi.menu_item_id " +
                                "WHERE oi.order_id = ?";
                        try (PreparedStatement itemStatement = connection.prepareStatement(sql)) {
                            itemStatement.setInt(1, order_id);
                            try (ResultSet itemResultSet = itemStatement.executeQuery()) {
                                int menuItemId = itemResultSet.getInt("menu_item_id");
                                for (Restaurant restaurant : restaurants) {
                                    if (restaurant.getRestaurantID() == restaurantId) {
                                        restaurantName = restaurant.getRestaurantName();
                                        for (MenuItem menuItem : restaurant.getMenu()) {
                                            if (menuItem.getMenuItemID() == menuItemId) {
                                                int quantity = itemResultSet.getInt("quantity");
                                                items.add(new CartItem(menuItem, quantity));
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            orders.add(new Order(deliveryAddress.toString(), items, date, status, restaurantName, order_id, rating));
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Database failed to fetch customer orders");
                }
                return orders;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    
        }