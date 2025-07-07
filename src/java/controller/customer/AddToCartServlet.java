package controller.customer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

import dao.*;
import dto.UserSession;
import model.*;

import util.IDGenerator;
import db.DBConnection;

public class AddToCartServlet extends HttpServlet {
    private ItemDAOImpl itemDAO;
    private CartItemDAO cartItemDAO;
    private DiscountAssignmentDAO discountAssignmentDAO;
    private DiscountDAO discountDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            itemDAO = new ItemDAOImpl(conn);
            cartItemDAO = new CartItemDAOimpl(conn);
            discountAssignmentDAO = new DiscountAssignmentDAOImpl(conn);
            discountDAO = new DicountDAOimpl(conn); // note: fix your class name spelling if needed
            
            System.out.println("AddToCartServlet initialized successfully.");
        } catch (Exception e) {
            System.err.println("DB init failed in AddToCartServlet: " + e.getMessage());
            throw new ServletException("DB init failed", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("🛒 AddToCartServlet triggered");

        try {
            HttpSession session = request.getSession();
            UserSession user = (UserSession) session.getAttribute("user");

            if (user == null) {
                System.out.println("User not logged in. Redirecting to login.jsp");
                response.sendRedirect("login.jsp");
                return;
            }

            Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
            if (cart == null) {
                System.out.println("Cart session attribute was null. Creating new cart map.");
                cart = new HashMap<>();
            } else {
                System.out.println("Cart loaded from session with " + cart.size() + " items.");
            }

            String itemId = request.getParameter("itemId");
            String quantityParam = request.getParameter("quantity");
            System.out.println("Request parameters - itemId: " + itemId + ", quantity: " + quantityParam);

            int quantity = 1; // default
            try {
                quantity = Integer.parseInt(quantityParam);
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity provided, defaulting to 1");
            }

            Item item = itemDAO.findById(itemId);
            if (item == null) {
                System.out.println("Item not found for id: " + itemId);
                response.sendRedirect("BookBrowseServlet?error=ItemNotFound");
                return;
            }

            System.out.println("Found item: " + item.getTitle());
            System.out.println("Original price: Rs. " + item.getPrice());

            // Find all applicable discount assignments (item, category, all)
            List<DiscountAssignment> assignments = discountAssignmentDAO.findAssignmentsForItem(itemId, item.getCategoryId());
            
            Discount bestDiscount = null;
            BigDecimal price = item.getPrice();

            for (DiscountAssignment assignment : assignments) {
                Discount d = discountDAO.findById(assignment.getDiscountId());
                if (d != null && d.isActive()) {
                    // Check if current date is between start and end
                    java.util.Date now = new java.util.Date();
                    if (!now.before(d.getStartDate()) && !now.after(d.getEndDate())) {
                        // Pick the best discount (highest discount_percent)
                        if (bestDiscount == null || d.getDiscountPercent() > bestDiscount.getDiscountPercent()) {
                            bestDiscount = d;
                        }
                    }
                }
            }

            if (bestDiscount != null) {
                BigDecimal discountAmount = price.multiply(BigDecimal.valueOf(bestDiscount.getDiscountPercent())).divide(BigDecimal.valueOf(100));
                price = price.subtract(discountAmount);
                System.out.println("Best discount applied: " + bestDiscount.getName() + " (" + bestDiscount.getDiscountPercent() + "%)");
                System.out.println("Discounted price: Rs. " + price);
                item.setHasDiscount(true);
                item.setDiscountedPrice(price);
                item.setDiscountLabel(bestDiscount.getName());
            } else {
                System.out.println("No discount applied.");
                item.setHasDiscount(false);
                item.setDiscountedPrice(price);
            }

            // Use discounted or original price for cart
            BigDecimal effectivePrice = item.isHasDiscount() ? item.getDiscountedPrice() : item.getPrice();

            CartItem cartItem = cart.get(itemId);
            if (cartItem == null) {
                System.out.println("Item not in cart yet, creating new CartItem.");
                cartItem = new CartItem(itemId, item.getTitle(), effectivePrice, quantity);
                cartItem.setImageUrl(item.getImageUrl());
                cartItem.setOriginalPrice(item.getPrice()); // original price for display
                System.out.println("New CartItem created with quantity: " + quantity);
            } else {
                int newQty = cartItem.getQuantity() + quantity;
                System.out.println("Item already in cart. Old quantity: " + cartItem.getQuantity() + ", adding " + quantity + ", new quantity: " + newQty);
                cartItem.setQuantity(newQty); // updates subtotal
                cartItem.setPrice(effectivePrice); // update price to reflect current discount
                System.out.println("Updated CartItem price to Rs. " + effectivePrice);
            }

            cart.put(itemId, cartItem);
            session.setAttribute("cart", cart);
            System.out.println("Cart session attribute updated. Total items now: " + cart.size());

            // Save to DB with actual price
            int nextCartNumber = cartItemDAO.getMaxCartItemNumber() + 1;
            String cartItemId = IDGenerator.generateId("cart", nextCartNumber);
            CartItem dbCartItem = new CartItem(cartItemId, user.getId(), itemId, quantity);
            dbCartItem.setPrice(effectivePrice);
            cartItemDAO.save(dbCartItem);

            System.out.println("Saved cart item to DB with ID: " + cartItemId + ", price: Rs. " + effectivePrice + ", quantity: " + quantity);

        } catch (Exception e) {
            System.err.println("Exception in AddToCartServlet.doPost: " + e.getMessage());
            e.printStackTrace();
        }

        response.sendRedirect("BookBrowseServlet");
    }
}
