package util;

import dto.ItemDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.io.File;

public class ItemRequestPasser {

    public static ItemDTO parseFromRequest(HttpServletRequest req) throws Exception {
        ItemDTO dto = new ItemDTO();

        String id = req.getParameter("id");
        if (id != null && !id.trim().isEmpty()) {
            dto.setId(id);
        }

        dto.setTitle(req.getParameter("title"));
        dto.setAuthor(req.getParameter("author"));
        dto.setDescription(req.getParameter("description"));

        try {
            String priceStr = req.getParameter("price");
            if (priceStr != null && !priceStr.isEmpty()) {
                dto.setPrice(new BigDecimal(priceStr));
            }
        } catch (NumberFormatException e) {
            System.out.println("[PASSER] Invalid price: " + req.getParameter("price"));
        }

        try {
            String stockStr = req.getParameter("stock");
            if (stockStr != null && !stockStr.isEmpty()) {
                dto.setStockQuantity(Integer.parseInt(stockStr));
            }
        } catch (NumberFormatException e) {
            System.out.println("[PASSER] Invalid stock quantity: " + req.getParameter("stock"));
        }

        // ✅ Handle file upload instead of URL input
        Part filePart = req.getPart("imageFile");
        if (filePart != null && filePart.getSize() > 0) {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            // Determine upload path inside /uploads
            String uploadPath = req.getServletContext().getRealPath("/uploads");
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // create /uploads if it doesn't exist
            }

            // Write file to disk
            filePart.write(uploadPath + File.separator + fileName);

            // Set relative URL (used in <img src="...">)
            dto.setImageUrl("uploads/" + fileName);
            System.out.println("[PASSER] Image saved to: uploads/" + fileName);
        } else {
            System.out.println("[PASSER] No image uploaded or empty.");
        }

        // Handle category
        String categoryId = req.getParameter("category");
        System.out.println("[PASSER] categoryId = " + categoryId);
        dto.setCategoryId(categoryId);

        return dto;
    }
}
