# TODO

## Fix org-admin Reject button + crash
- [x] Remove access-code generation from `AdminController` org creation.
- [x] Fix compilation error in `ValidationController` (canAccessOrganization signature mismatch).
- [x] Confirm `mvn compile` succeeds.

## Admin dashboard: latest transactions button
- [ ] Add a button/link on `/admin/dashboard` to latest transactions.
- [ ] Implement endpoint + service/controller query for newest-to-oldest transactions.
- [ ] Display transaction list with associated user (fullName/email) in a template.
- [ ] Compile and verify routes work.

