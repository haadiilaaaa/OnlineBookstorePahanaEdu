package strategy.admin.item;

import dto.ItemDTO;
import service.admin.ItemService;
import util.IDGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Paths;

public class AddItemStrategy implements ItemActionStrategy {
    private final ItemService itemService;
    private final IDGenerator<String> itemIdGenerator; // New dependency

    public AddItemStrategy(ItemService itemService, IDGenerator<String> itemIdGenerator) {
        this.itemService = itemService;
        this.itemIdGenerator = itemIdGenerator;
    }

    @Override
    public StrategyResult execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.setCharacterEncoding("UTF-8");

        String title = req.getParameter("title");
        String author = req.getParameter("author");
        String description = req.getParameter("description");
        BigDecimal price = new BigDecimal(req.getParameter("price"));
        int stock = Integer.parseInt(req.getParameter("stock"));
        String categoryId = req.getParameter("category");

        System.out.println("📦 categoryId from form: " + categoryId);

        // 🔄 Step 1: Handle file upload
        Part imagePart = req.getPart("imageFile");
        String fileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();

        String uploadPath = req.getServletContext().getRealPath("/uploads");

        // 🔒 Ensure directory exists
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        // 🔄 Step 2: Save file
        String savedFilePath = uploadPath + File.separator + fileName;
        try (InputStream input = imagePart.getInputStream();
             FileOutputStream output = new FileOutputStream(savedFilePath)) {
            input.transferTo(output);
        }

        // 🖼️ Generate relative URL (for <img src>)
        String imageUrl = req.getContextPath() + "/uploads/" + fileName;

        // ✅ Step 3: Populate DTO
        ItemDTO dto = new ItemDTO();
        dto.setTitle(title);
        dto.setAuthor(author);
        dto.setDescription(description);
        dto.setPrice(price);
        dto.setStockQuantity(stock);
        dto.setCategoryId(categoryId);
        dto.setImageUrl(imageUrl);

        // Use the injected IDGenerator to get the new ID
        dto.setId(itemIdGenerator.generate());

        itemService.addItem(dto);

        // Optional: add a flash attribute or log if needed
        req.getSession().setAttribute("success", "Item added successfully.");

        // ✅ Redirect back to servlet to avoid form resubmission (Post/Redirect/Get pattern)
        return new StrategyResult("AddItemServlet", true);
    }
}