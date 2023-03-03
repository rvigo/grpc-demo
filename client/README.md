# gRPC Client demo

### create a new person:
```bash
curl \
-H "Content-Type: application/json" \
-d '{"addresses": [{ "street": "street", "city": "city", "type": "HOME"}],"name": "Name","cpf": "4321","email": "name@email.com"}' \
-X POST http://localhost:8080/person
```

### find a person:

```bash
curl -H 'Content-Type: application/json' localhost:8080/person/4321
```

### todo:

- [ ] properly handle exceptions
- [ ] add a controller advice
