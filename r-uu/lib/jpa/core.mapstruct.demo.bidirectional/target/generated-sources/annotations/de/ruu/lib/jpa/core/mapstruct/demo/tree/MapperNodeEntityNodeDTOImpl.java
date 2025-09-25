package de.ruu.lib.jpa.core.mapstruct.demo.tree;

import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-23T20:24:54+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
class MapperNodeEntityNodeDTOImpl extends MapperNodeEntityNodeDTO {

    @Override
    NodeDTO map(NodeEntity input) {
        if ( input == null ) {
            return null;
        }

        NodeDTO nodeDTO = new NodeDTO();

        beforeMapping( input, nodeDTO );

        return nodeDTO;
    }

    @Override
    NodeEntity map(NodeDTO input) {
        if ( input == null ) {
            return null;
        }

        NodeEntity nodeEntity = new NodeEntity();

        beforeMapping( input, nodeEntity );

        return nodeEntity;
    }
}
