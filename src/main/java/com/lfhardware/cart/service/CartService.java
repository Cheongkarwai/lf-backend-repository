package com.lfhardware.cart.service;

import com.lfhardware.cart.dto.CartDTO;
import com.lfhardware.cart.dto.CartItemDTO;
import com.lfhardware.cart.mapper.CartDetailsMapper;
import com.lfhardware.cart.repository.ICartDetailsRepository;
import com.lfhardware.cart.repository.ICartRepository;
import com.lfhardware.product.repository.IProductRepository;
import com.lfhardware.stock.repository.IStockRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.stage.Stage;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CartService implements ICartService {

    private final ICartDetailsRepository cartDetailsRepository;

    private final ICartRepository cartRepository;

    private final IProductRepository productRepository;

    private final IStockRepository stockRepository;

    private final Stage.SessionFactory sessionFactory;

    private final CartDetailsMapper cartDetailsMapper;

    private final CacheManager cacheManager;


    public CartService(ICartDetailsRepository cartDetailsRepository, ICartRepository cartRepository, IProductRepository productRepository,
                       IStockRepository stockRepository,
                       Stage.SessionFactory sessionFactory,  CartDetailsMapper cartDetailsMapper, CacheManager cacheManager) {
        this.cartDetailsRepository = cartDetailsRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.stockRepository = stockRepository;
        this.sessionFactory = sessionFactory;
        this.cartDetailsMapper = cartDetailsMapper;
        this.cacheManager = cacheManager;
    }

    @PreAuthorize("isFullyAuthenticated()")
    public Mono<Void> addItemToCart(CartItemDTO cartItem) {
        return Mono.empty();
//        return ReactiveSecurityContextHolder.getContext().map(securityContext -> (Jwt) securityContext.getAuthentication().getPrincipal())
//                .flatMap(jwt -> userService.findById(jwt.getSubject()))
//                .flatMap(userDTO -> Mono.fromCompletionStage(sessionFactory.withSession(session -> cartRepository.findByUsername(session, userDTO.getUsername()))
//                        .thenCombine(sessionFactory.withSession(session -> stockRepository.findByProductIdAndSize(session, cartItem.productId(), cartItem.size())), (cart, stock) -> {
//                            //Save items into cart
//                            log.info("Saving items into cart");
//                            CartDetails cartDetails = new CartDetails(cartItem.quantity(), new CartDetailsId(stock.getId(), cart.getId()), stock, cart);
//                            return cartDetails;
//                        }).thenCompose(result -> sessionFactory.withTransaction((session, transaction) -> cartDetailsRepository.merge(session, result)
//                                .thenAccept(cartDetails -> cacheManager.getCache("cart").evict(userDTO.getUsername()))))).then());
    }

    @PreAuthorize("isFullyAuthenticated()")
    @PostAuthorize("hasRole('ADMINISTRATOR') || returnedObject.username == authentication.name")
    @Override
    public Mono<CartDTO> findCart() {
        return Mono.empty();
//        return ReactiveSecurityContextHolder.getContext().map(securityContext -> (Jwt) securityContext.getAuthentication().getPrincipal())
//                .flatMap(jwt -> userService.findById(jwt.getSubject())
//                        .flatMap(userDTO -> userService.findByEmailAddress(userDTO.getUsername())))
//                .flatMap(userDTO -> {
//                    CartDTO cacheCartDTO = cacheManager.getCache("cart").get(userDTO.getUsername(), CartDTO.class);
//                    if (cacheCartDTO == null) {
//                        return Mono.fromCompletionStage(sessionFactory.withSession(session -> cartRepository.findAllCartStocksByUsername(session, userDTO.getUsername())
//                                .thenApply(cart -> {
//                                    if(cart != null){
//                                        Set<ItemDTO> itemDTOS = cart.getCartDetails().stream().map(cartDetailsMapper::mapToItemDTO).collect(Collectors.toSet());
//                                        BigDecimal subtotal = SalesUtil.calculateSubtotal(itemDTOS);
//                                        CartDTO cartDTO = new CartDTO(subtotal, SalesUtil.calculateAfterSalesTax(subtotal), BigDecimal.valueOf(0), BigDecimal.valueOf(0), itemDTOS, userDTO.getUsername());
//                                        cacheManager.getCache("cart").put(userDTO.getUsername(), cartDTO);
//                                        return cartDTO;
//                                    }
//                                    return new CartDTO();
//                                })));
//                    }
//                    return Mono.just(cacheCartDTO);
//                });

    }

    @PreAuthorize("isFullyAuthenticated() ||  hasRole('ADMINISTRATOR')")
    @Override
    public Mono<Void> deleteCartItem(Long stockId, Long cartId) {
        return Mono.empty();
//        return ReactiveSecurityContextHolder.getContext().map(securityContext -> (Jwt) securityContext.getAuthentication().getPrincipal())
//                .flatMap(jwt -> userService.findById(jwt.getSubject())).flatMap(userDTO -> Mono.fromCompletionStage(
//                        sessionFactory.withTransaction((session, transaction) -> cartDetailsRepository.deleteById(session, new CartDetailsId(stockId, cartId))
//                                .thenApply(e -> {
//                                    cacheManager.getCache("cart").evict(userDTO.getUsername());
//                                    return session.flush();
//                                }))))
//                .then();
    }

    @PreAuthorize("isFullyAuthenticated()")
    @Override
    public Mono<Void> updateCartItemQuantity(Long stockId, Long cartId, CartItemDTO cartItem) {
        return Mono.empty();
//        return ReactiveSecurityContextHolder.getContext().map(securityContext -> (Jwt) securityContext.getAuthentication().getPrincipal())
//                .flatMap(jwt -> userService.findById(jwt.getSubject()))
//                .flatMap(userDTO -> Mono.fromCompletionStage(sessionFactory.withSession(session -> cartDetailsRepository.updateCartDetailsQuantityById(session, cartItem.quantity(), new CartDetailsId(stockId, cartId))
//                        .thenApply(e -> {
//                            cacheManager.getCache("cart").evict(userDTO.getUsername());
//                            return session.flush();
//                        })))).then();
    }
}
