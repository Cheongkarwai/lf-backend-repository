package com.lfhardware.form.repository;

import com.lfhardware.form.domain.Form;
import com.lfhardware.form.dto.FormPageRequest;
import com.lfhardware.core.repository.CrudRepository;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface IFormRepository extends CrudRepository<Form, Long> {

    CompletionStage<List<Form>> findAll(Stage.Session session, FormPageRequest pageInfo, List<Long> formIds);

    CompletionStage<Long> count(Stage.Session session);

    CompletionStage<Long> countInIds(Stage.Session session, List<Long> formIds);
}
