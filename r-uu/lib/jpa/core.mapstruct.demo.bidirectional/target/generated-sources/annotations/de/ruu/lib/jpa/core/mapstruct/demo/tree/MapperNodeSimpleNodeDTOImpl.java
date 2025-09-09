package de.ruu.lib.jpa.core.mapstruct.demo.tree;

import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-09T07:17:31+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
class MapperNodeSimpleNodeDTOImpl extends MapperNodeSimpleNodeDTO {

    @Override
    NodeDTO map(NodeSimple input) {
        if ( input == null ) {
            return null;
        }

        NodeDTO nodeDTO = new NodeDTO();

        beforeMapping( input, nodeDTO );

        return nodeDTO;
    }

    @Override
    NodeSimple map(NodeDTO input) {
        if ( input == null ) {
            return null;
        }

        NodeSimple nodeSimple = new NodeSimple();

        beforeMapping( input, nodeSimple );

        return nodeSimple;
    }
}
