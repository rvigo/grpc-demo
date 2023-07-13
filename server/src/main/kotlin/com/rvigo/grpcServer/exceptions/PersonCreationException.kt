package com.rvigo.grpcServer.exceptions

import com.rvigo.grpcServer.models.Person

class PersonCreationException(person: Person): RuntimeException("cannot create person $person")
