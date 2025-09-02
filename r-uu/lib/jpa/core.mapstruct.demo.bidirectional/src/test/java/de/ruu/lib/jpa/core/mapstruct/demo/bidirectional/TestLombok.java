package de.ruu.lib.jpa.core.mapstruct.demo.bidirectional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestLombok
{
	@Test void emptyDepartmentDTO()
	{
		DepartmentDTO department = new DepartmentDTO();

		assertThat(department.id()         , is(Matchers.nullValue()));
		assertThat(department.name()       , is(Matchers.nullValue()));
		assertThat(department.description(), is(Matchers.nullValue()));
	}

	@Test void emptyEmployeeDTO()
	{
		EmployeeDTO employee = new EmployeeDTO();

		assertThat(employee.id()        , is(Matchers.nullValue()));
		assertThat(employee.name()      , is(Matchers.nullValue()));
		assertThat(employee.department(), is(Matchers.nullValue()));
	}

	@Test void emptyDepartmentEntity()
	{
		DepartmentEntity department = new DepartmentEntity();

		assertThat(department.id()         , is(Matchers.nullValue()));
		assertThat(department.name()       , is(Matchers.nullValue()));
		assertThat(department.description(), is(Matchers.nullValue()));
	}

	@Test void emptyEmployeeEntity()
	{
		EmployeeEntity employee = new EmployeeEntity();

		assertThat(employee.id()        , is(Matchers.nullValue()));
		assertThat(employee.name()      , is(Matchers.nullValue()));
		assertThat(employee.department(), is(Matchers.nullValue()));
	}

	@Test void invalidNameDepartmentDTO()
	{
		String name = null;

		assertThrows(NullPointerException.class, () -> new DepartmentDTO(name));
	}

	@Test void invalidNameEmployeeDTO()
	{
		String name = null;
		DepartmentDTO department = new DepartmentDTO("name");

		assertThrows(NullPointerException.class, () -> new EmployeeDTO(department, name));
	}

	@Test void invalidDepartmentEmployeeDTO()
	{
		String name = "name";
		DepartmentDTO department = null;

		assertThrows(NullPointerException.class, () -> new EmployeeDTO(department, name));
	}

	@Test void invalidNameDepartmentEntity()
	{
		String name = null;

		assertThrows(NullPointerException.class, () -> new DepartmentEntity(name));
	}

	@Test void invalidNameEmployeeEntity()
	{
		String name = null;
		DepartmentEntity department = new DepartmentEntity("name");

		assertThrows(NullPointerException.class, () -> new EmployeeEntity(department, name));
	}

	@Test void validDepartmentDTO()
	{
		String        name           = "name";
		DepartmentDTO departmentDTO  = new DepartmentDTO(name);

		assertThat(departmentDTO.name()       , is(name));
		assertThat(departmentDTO.description(), is(Matchers.nullValue()));
	}

	@Test void validEmployeeInDepartmentDTO()
	{
		String        name       = "name";
		DepartmentDTO department = new DepartmentDTO(name);
		EmployeeDTO   employee   = new EmployeeDTO(department, name);

		assertThat(employee.name()      , is(name));
		assertThat(employee.department(), is(department));
	}

	@Test void invalidEmptyDepartmentNameSetting()
	{
		DepartmentEntity department = new DepartmentEntity();
		assertThrows(IllegalArgumentException.class, () -> department.name(""));
	}

	@Test void invalidNullDepartmentNameSetting()
	{
		DepartmentEntity department = new DepartmentEntity();
		assertThrows(NullPointerException.class, () -> department.name(null));
	}
}