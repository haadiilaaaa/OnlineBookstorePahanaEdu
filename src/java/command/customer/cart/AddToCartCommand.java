 package command.customer.cart;

import dto.AddToCartRequestDTO;
import dto.ItemDTO;
import dto.UserSession;
import service.admin.ItemService;
import service.common.Validator;
import util.ValidationException;
import javax.servlet.http.HttpSession;
import service.customer.CartFacade;
import static util.contannts.ErrorMessages.*;

public class AddToCartCommand {

    private final Validator<AddToCartRequestDTO> validator;
    private final ItemService itemService;
    private final CartFacade cartFacade;

    public AddToCartCommand(Validator<AddToCartRequestDTO> validator, ItemService itemService, CartFacade cartFacade) {
        this.validator = validator;
        this.itemService = itemService;
        this.cartFacade = cartFacade;
    }

    public void execute(AddToCartRequestDTO dto, UserSession user, HttpSession session) throws ValidationException, Exception {
        validator.validate(dto);

        ItemDTO item = itemService.getItemById(dto.getItemId());
        if (item == null) {
            throw new Exception(ITEM_NOT_FOUND);
        }

        int quantity = Integer.parseInt(dto.getQuantity());

        cartFacade.addItemToCart(user.getId(), item, quantity, session);
    }
}