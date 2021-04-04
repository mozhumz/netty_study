namespace java com.hyj.thrift

typedef i32 int
typedef i64 long
typedef string String

struct Person{
    1: int age
    2: String name
}

service MyPersonService{
    Person getPersonByName(1:String name)
    void savePerson(1:Person person)
}