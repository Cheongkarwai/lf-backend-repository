package com.lfhardware.form.repository;

import com.lfhardware.form.domain.Form;
import com.lfhardware.form.domain.FormId;
import com.lfhardware.form.dto.FormPageRequest;
import com.lfhardware.shared.CrudRepository;
import com.lfhardware.shared.PageInfo;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface IFormRepository extends CrudRepository<Form, FormId> {

    CompletionStage<List<Form>> findAll(Stage.Session session, FormPageRequest pageInfo, List<FormId> formIds);

    CompletionStage<Long> count(Stage.Session session);

    CompletionStage<Long> countInIds(Stage.Session session, List<FormId> formIds);
}
