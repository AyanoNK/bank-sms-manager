# bank-purchases-manager
Mobile application to manage your bank purchases. You get the statements in real time thanks to bank SMS statements.

## Considerations
- The app runs on Android 10 or above.
- The app reads your text messages to get the value of the purchases, but it does not store all your messages. It merely parses the messages and gets relevant information.

## Roadmap
### Investigating the feasibility of the project
- [x] Investigate Android permissions (to see if it's even possible to read text messages when not running the app).
- [x] Initialize the project.
- [x] Recognize that text messages are being received outside the application.
### Adding the main feature
- [x] Parse the text messages to recognize if they come from a bank and get the amount paid.
- [x] Save the amount as a persistable value and sum the value of new messages.
- [x] Display the total amount for a period of time (a month for now) on a Widget.
### Adding personalization
- [x] To the text messaging, add parsing of timestamp, bank name, and purchase concept.
- [ ] Within the main app, add a CRUD for messages. We can't be sure that parsing was 100% effective, so I should be able to add my own SMS messages if any were missed, or if any were incorrectly added.
- [ ] Add styles to the app.
### Expanding database realms
- [ ] Add authentication with TO BE DETERMINED auth provider (Firebase/Supabase probs). This will happen in a separate repository.
- [ ] Sync data between the app and the new backend service.
### Adding value to data
- [ ] Consume backend service to obtain metrics.
- [ ] Display metrics with cute graphs or something.

## Resources
- [Interpret SMS](https://youtu.be/9fIiQ9YQ7BI)

### Useful links down the line
- [Bunch of widget examples](https://github.com/android/user-interface-samples/tree/main/AppWidget).
- [Make the widget pretty](https://developer.android.com/develop/ui/views/appwidgets/enhance).
- [Run a background operation within the app](https://developer.android.com/training/run-background-service/create-service).
