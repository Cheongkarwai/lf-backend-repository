package com.lfhardware.faq.service;

import com.lfhardware.city.dto.CityDTO;
import com.lfhardware.faq.domain.Faq;
import com.lfhardware.faq.dto.FaqDTO;
import com.lfhardware.faq.mapper.FaqMapper;
import com.lfhardware.faq.repository.IFaqRepository;
import org.hibernate.reactive.stage.Stage;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Service
public class FaqService implements IFaqService {

    private final Stage.SessionFactory sessionFactory;
    private final IFaqRepository faqRepository;

    private final FaqMapper faqMapper;

    private final CacheManager cacheManager;

    public FaqService(Stage.SessionFactory sessionFactory, IFaqRepository faqRepository,
                      FaqMapper faqMapper, CacheManager cacheManager) {
        this.sessionFactory = sessionFactory;
        this.faqRepository = faqRepository;
        this.faqMapper = faqMapper;
        this.cacheManager = cacheManager;
    }

    @Override
    public Mono<List<FaqDTO>> findAll() {

        return Mono.justOrEmpty(cacheManager.getCache("faqCache").get("faqs", (Callable<List<FaqDTO>>) ArrayList::new))
                .flatMap(faqs -> faqs.isEmpty() ? Mono.empty() : Mono.just(faqs))
                .switchIfEmpty(Mono.defer(() ->
                        Mono.fromCompletionStage(sessionFactory.withSession(session -> faqRepository.findAll(session)
                                .thenApply(faqs -> {
                                    List<FaqDTO> faqDTOS = faqs.stream()
                                            .map(faqMapper::mapToFaqDTO)
                                            .toList();
                                    Objects.requireNonNull(cacheManager.getCache("faqCache")).put("faqs", faqDTOS);
                                    return faqDTOS;
                                })))));
    }

    @Override
    public Mono<FaqDTO> save(FaqDTO faqDTO) {
        return Mono.fromCallable(() -> faqMapper.mapToFaq(faqDTO))
                .flatMap(faq -> Mono.fromCompletionStage(sessionFactory.withTransaction(session -> faqRepository.save(session, faq)
                        .thenApply(v -> {
                            Objects.requireNonNull(cacheManager.getCache("faqCache")).invalidate();
                            return faqMapper.mapToFaqDTO(faq);
                        }))));
    }

    @Override
    public Mono<Void> updateById(Long id, FaqDTO faqDTO) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> faqRepository.findById(session, id)
                        .thenCompose(faq -> {
                            faq.setTitle(faqDTO.getTitle());
                            faq.setDescription(faqDTO.getDescription());
                            return faqRepository.save(session, faq);
                        }).thenApply(v -> Objects.requireNonNull(cacheManager.getCache("faqCache")).invalidate())))
                .then();
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> faqRepository.deleteById(session, id)
                        .thenApply(v -> Objects.requireNonNull(cacheManager.getCache("faqCache")).invalidate())))
                .then();
    }
}
