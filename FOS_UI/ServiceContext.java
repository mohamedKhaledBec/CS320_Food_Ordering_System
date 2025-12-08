package FOS_UI;

import FOS_CORE.IAccountService;
import FOS_CORE.ICartService;
import FOS_CORE.ILoginService;
import FOS_CORE.IManagerService;
import FOS_CORE.IOrderService;
import FOS_CORE.IPaymentService;
import FOS_CORE.IRestaurantService;

import FOS_CORE.AccountService;
import FOS_CORE.CartService;
import FOS_CORE.ManagerService;
import FOS_CORE.OrderService;
import FOS_CORE.PaymentService;
import FOS_CORE.RestaurantService;
//worked on by Umair Ahmad
public final class ServiceContext {

    private static final IAccountService accountService = new AccountService();
    private static final IRestaurantService restaurantService = new RestaurantService();
    private static final ICartService cartService = new CartService();
    private static final IOrderService orderService = new OrderService();
    private static final IManagerService managerService = new ManagerService();
    private static final IPaymentService paymentService = new PaymentService();

    private ServiceContext() {}

    public static IAccountService getAccountService() {
        return accountService;
    }

    public static IRestaurantService getRestaurantService() {
        return restaurantService;
    }

    public static ICartService getCartService() {
        return cartService;
    }

    public static IOrderService getOrderService() {
        return orderService;
    }

    public static IManagerService getManagerService() {
        return managerService;
    }

    public static IPaymentService getPaymentService() {
        return paymentService;
    }
}
