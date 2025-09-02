# jeeeraaah datamodel extended

## tasks and plannings

Task planning is subject to many influences that are often not fully known at the time of planning. As a result, plans are constantly being adjusted. For future planning, it can be interesting to see how planning assumptions for a task have changed.

Therefore ```Planning```s can be associated to ```Task```s in order to track changes in ```Task.start``` and ```Task.end``` until ```Task.closed``` was not set to ```true```.

```puml
@startuml

!pragma layout smetana
skinparam linetype ortho

interface Task { }

interface Planning
{
  Task task()
  LocalDate timestamp()
  Optional<LocalDate> start()
  Optional<LocalDate> end()
}

Planning "many" *-l>  "1" Task : "   task   "

@enduml
```

## tasks and expenditure

```puml
@startuml

!pragma layout smetana
skinparam linetype ortho

interface Task { }

interface Expenditure
{
  Task task()
  LocalDate timestamp()
  Long amount()
}

interface Estimate
{
  Expenditure expenditure()
  LocalDate timestamp()
  Long amount()
}

Expenditure "many" *-l>  "   1" Task : "   task   "
Estimate    "many" *-l>  "   1" Expenditure : "   expenditure   "

@enduml
```

Task planning is subject to many influences that are often not fully known at the time of planning. As a result, plans are constantly being adjusted. For future planning, it can be interesting to see how planning assumptions for a task have changed.

Therefore ```Planning```s can be associated to ```Estimate```s (indirectly linked via ```Expenditure```) in order to track changes in ```Expenditure.amount``` until ```Task.closed``` was not set to ```true```.
