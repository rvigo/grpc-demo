package com.rvigo.grpcClient.exceptions

import com.rvigo.grpcClient.models.Person

class PersonCreationException(person: Person): RuntimeException("cannot create person $person")
