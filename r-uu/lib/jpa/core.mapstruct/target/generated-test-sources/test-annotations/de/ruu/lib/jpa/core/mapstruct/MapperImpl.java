package de.ruu.lib.jpa.core.mapstruct;

import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-10T08:39:35+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
class MapperImpl extends Mapper {

    @Override
    SimpleMappedEntity map(SimpleMappedDTO input) {
        if ( input == null ) {
            return null;
        }

        SimpleMappedEntity simpleMappedEntity = lookupOrCreate( input );

        beforeMapping( input, simpleMappedEntity );

        return simpleMappedEntity;
    }

    @Override
    SimpleMappedDTO map(SimpleMappedEntity input) {
        if ( input == null ) {
            return null;
        }

        SimpleMappedDTO simpleMappedDTO = lookupOrCreate( input );

        beforeMapping( input, simpleMappedDTO );

        return simpleMappedDTO;
    }
}
