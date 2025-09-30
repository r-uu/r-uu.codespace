package de.ruu.lib.jpa.core.mapstruct.demo.tree;

import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-30T22:27:38+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
class MapperNodeSimpleNodeEntityImpl extends MapperNodeSimpleNodeEntity {

    @Override
    NodeEntity map(NodeSimple input) {
        if ( input == null ) {
            return null;
        }

        NodeEntity nodeEntity = new NodeEntity();

        beforeMapping( input, nodeEntity );

        return nodeEntity;
    }

    @Override
    NodeSimple map(NodeEntity input) {
        if ( input == null ) {
            return null;
        }

        NodeSimple nodeSimple = new NodeSimple();

        beforeMapping( input, nodeSimple );

        return nodeSimple;
    }
}
