package com.rvigo.grpcPoc.exceptions

import com.rvigo.grpcPoc.models.Person

class PersonCreationException(person: Person): RuntimeException("cannot create person $person")
