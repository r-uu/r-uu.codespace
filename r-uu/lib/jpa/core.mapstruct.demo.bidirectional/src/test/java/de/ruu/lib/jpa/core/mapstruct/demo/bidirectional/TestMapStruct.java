package de.ruu.lib.jpa.core.mapstruct.demo.bidirectional;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestMapStruct
{
	@Test void mapEmptyDepartmentDTO()
	{
		DepartmentDTO department = new DepartmentDTO();
		assertThrows(NullPointerException.class, () -> Mapper.INSTANCE.map(department));
	}

	@Test void mapEmptyDepartmentEntity()
	{
		DepartmentEntity department = new DepartmentEntity();
		assertThrows(NullPointerException.class, () -> Mapper.INSTANCE.map(department));
	}

	@Test void mapEmptyEmployeeDTO()
	{
		EmployeeDTO employee = new EmployeeDTO();
		assertThrows(NullPointerException.class, () -> Mapper.INSTANCE.map(employee));
	}

	@Test void mapEmptyEmployeeEntity()
	{
		EmployeeEntity employee = new EmployeeEntity();
		assertThrows(NullPointerException.class, () -> Mapper.INSTANCE.map(employee));
	}

	@Test void mapInvalidNamedDepartmentDTO()
	{
		String name = null;
		assertThrows(NullPointerException.class, () -> Mapper.INSTANCE.map(new DepartmentDTO(name)));
	}

	@Test void mapInvalidNamedEmployeeDTO()
	{
		String name = null;
		assertThrows(NullPointerException.class, () -> Mapper.INSTANCE.map(new EmployeeDTO(new DepartmentDTO("name"), name)));
	}

	@Test void mapInvalidNamedDepartmentEntity()
	{
		String name = null;
		assertThrows(NullPointerException.class, () -> Mapper.INSTANCE.map(new DepartmentEntity(name)));
	}

	@Test void mapInvalidNamedEmployeeEntity()
	{
		String           name       = null;
		DepartmentEntity department = new DepartmentEntity("name");
		assertThrows(NullPointerException.class,() -> Mapper.INSTANCE.map(new EmployeeEntity(department, name)));
	}

	@Test void mapValidDepartmentDTO()
	{
		String        name       = "name";
		DepartmentDTO department = new DepartmentDTO(name);
		DepartmentEntity departmentEntity = Mapper.INSTANCE.map(department);
		assertThat(departmentEntity       , is(not(nullValue())));
		assertThat(departmentEntity.name(), is(name));
	}

	@Test void mapValidDepartmentEntity()
	{
		String           name       = "name";
		DepartmentEntity department = new DepartmentEntity(name);
		DepartmentDTO departmentDTO = Mapper.INSTANCE.map(department);
		assertThat(departmentDTO       , is(not(nullValue())));
		assertThat(departmentDTO.id()  , is(department.getId()));
		assertThat(departmentDTO.name(), is(name));
	}

	@Test void mapValidEmployeeDTO()
	{
		String         name       = "name";
		DepartmentDTO  department = new DepartmentDTO(name);
		EmployeeDTO    employee   = new EmployeeDTO(department, name);
		EmployeeEntity employeeEntity = Mapper.INSTANCE.map(employee);
		assertThat(employeeEntity                    , is(not(nullValue())));
		assertThat(employeeEntity.name()             , is(name));
		assertThat(employeeEntity.department().name(), is(name));
	}

	@Test void mapValidEmployeeEntity()
	{
		String            name       = "name";
		DepartmentEntity  department = new DepartmentEntity(name);
		EmployeeEntity    employee   = new EmployeeEntity(department, name);
		EmployeeDTO employeeDTO = Mapper.INSTANCE.map(employee);
		assertThat(employeeDTO                    , is(not(nullValue())));
		assertThat(employeeDTO.name()             , is(name));
		assertThat(employeeDTO.department().name(), is(name));
	}

	@Test void mapValidDepartmentDTOWithEmployees()
	{
		String        name              = "name";
		DepartmentDTO department        = new DepartmentDTO(name);
		int           numberOfEmployees = 3;

		for (int i = 0; i < numberOfEmployees; i++)
		{
			department.add(new EmployeeDTO(department, "name." + i));
		}

		DepartmentEntity departmentEntity = Mapper.INSTANCE.map(department);

		assertThat(departmentEntity                                 , is(not(nullValue())));
		assertThat(departmentEntity.optionalEmployees()             , is(not(nullValue())));
		assertThat(departmentEntity.optionalEmployees().isPresent() , is(true));
		assertThat(departmentEntity.optionalEmployees().get().size(), is(numberOfEmployees));
	}

	@Test void mapValidDepartmentEntityWithEmployees()
	{
		String           name              = "name";
		DepartmentEntity department        = new DepartmentEntity(name);
		int              numberOfEmployees = 3;

		for (int i = 0; i < numberOfEmployees; i++)
		{
			department.add(new EmployeeEntity(department, "name." + i));
		}

		DepartmentDTO departmentDTO = Mapper.INSTANCE.map(department);

		assertThat(departmentDTO                                 , is(not(nullValue())));
		assertThat(departmentDTO.optionalEmployees()             , is(not(nullValue())));
		assertThat(departmentDTO.optionalEmployees().isPresent() , is(true));
		assertThat(departmentDTO.optionalEmployees().get().size(), is(numberOfEmployees));
	}
}