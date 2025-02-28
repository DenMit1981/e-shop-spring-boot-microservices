package com.denmit.eshop.attachmentservice.mapper;

import com.denmit.eshop.attachmentservice.dto.AttachmentNameResponseDto;
import com.denmit.eshop.attachmentservice.dto.AttachmentResponseDto;
import com.denmit.eshop.attachmentservice.model.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    @Mapping(target = "fileSize", source = "entity.fileSize", qualifiedByName = "getSizeInByte")
    AttachmentResponseDto toDto(Attachment entity);

    List<AttachmentResponseDto> toDtos(List<Attachment> attachments);

    AttachmentNameResponseDto toNameDto(Attachment entity);

    @Named("getSizeInByte")
    default String getSizeInByte(Long size) {
        if (size == null) {
            return "";
        }

        String[] units = new String[]{"B", "KB", "MB"};
        int unitIndex = (int) (Math.log10(size) / 3);
        double sizeInUnit = size / Math.pow(1024, unitIndex);
        NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
        nf.setMaximumFractionDigits(1);

        return String.format("%s %s", nf.format(sizeInUnit), units[unitIndex]);
    }
}
