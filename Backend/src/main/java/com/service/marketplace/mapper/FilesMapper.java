package com.service.marketplace.mapper;

import com.service.marketplace.dto.request.FilesRequest;
import com.service.marketplace.dto.request.ReviewRequest;
import com.service.marketplace.dto.response.FilesResponse;
import com.service.marketplace.dto.response.ReviewResponse;
import com.service.marketplace.persistence.entity.Files;
import com.service.marketplace.persistence.entity.Review;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FilesMapper {
    List<FilesResponse> toFilesResponseList(List<Files> files);

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "url", source = "request.url")
    @Mapping(target = "service", source = "service")
    Files filesRequestToFiles(FilesRequest request, Service service);

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "url", source = "request.url")
    @Mapping(target = "review", source = "review")
    Files filesRequestToFiles(FilesRequest request, Review review);

    @Mapping(target = "serviceId", source = "files.service.id")
    @Mapping(target = "reviewId", source = "files.review.id")
    FilesResponse filesToFilesResponse(Files files);
}
