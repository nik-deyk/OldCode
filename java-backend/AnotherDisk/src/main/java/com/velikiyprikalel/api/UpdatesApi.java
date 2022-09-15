/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (6.1.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package com.velikiyprikalel.api;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import com.velikiyprikalel.model.SystemItemHistoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Optional;

@Validated
@RequestMapping("${openapi.yetAnotherDiskOpen.base-path:}")
public interface UpdatesApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /updates
     * Получение списка **файлов**, которые были обновлены за последние 24 часа
     * включительно [date - 24h, date] от времени переданном в запросе.
     *
     * @param date Дата и время запроса. Дата должна обрабатываться согласно ISO
     *             8601 (такой придерживается OpenAPI). Если дата не удовлетворяет
     *             данному формату, необходимо отвечать 400 (required)
     * @return Список элементов, которые были обновлены. (status code 200)
     *         or Невалидная схема документа или входные данные не верны. (status
     *         code 400)
     */
    @RequestMapping(method = RequestMethod.GET, value = "/updates", produces = { "application/json" })
    default ResponseEntity<SystemItemHistoryResponse> updatesGet(
            @NotNull @Valid @RequestParam(value = "date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime date) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
