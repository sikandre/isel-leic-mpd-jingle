package org.isel.jingle;

import org.isel.jingle.dto.AlbumDto;

public class ResultAlbumDto {
    private final AlbumDto album;

    public ResultAlbumDto(AlbumDto album) {
        this.album = album;
    }

    public AlbumDto getAlbum() {
        return album;
    }
}
