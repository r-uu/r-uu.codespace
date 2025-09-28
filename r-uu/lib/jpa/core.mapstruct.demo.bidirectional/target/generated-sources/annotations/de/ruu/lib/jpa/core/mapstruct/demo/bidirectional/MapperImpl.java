package de.ruu.lib.jpa.core.mapstruct.demo.bidirectional;

import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-28T23:07:41+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 24.0.2 (Oracle Corporation)"
)
class MapperImpl extends Mapper {

    @Override
    DepartmentEntity map(DepartmentDTO input) {
        if ( input == null ) {
            return null;
        }

        DepartmentEntity departmentEntity = lookupOrCreate( input );

        beforeMapping( input, departmentEntity );

        departmentEntity.description( input.getDescription() );

        return departmentEntity;
    }

    @Override
    DepartmentDTO map(DepartmentEntity input) {
        if ( input == null ) {
            return null;
        }

        DepartmentDTO departmentDTO = lookupOrCreate( input );

        beforeMapping( input, departmentDTO );

        return departmentDTO;
    }

    @Override
    EmployeeEntity map(EmployeeDTO input) {
        if ( input == null ) {
            return null;
        }

        EmployeeEntity employeeEntity = lookupOrCreate( input );

        beforeMapping( input, employeeEntity );

        employeeEntity.name( input.getName() );

        return employeeEntity;
    }

    @Override
    EmployeeDTO map(EmployeeEntity input) {
        if ( input == null ) {
            return null;
        }

        EmployeeDTO employeeDTO = lookupOrCreate( input );

        beforeMapping( input, employeeDTO );

        employeeDTO.name( input.getName() );

        return employeeDTO;
    }
}
