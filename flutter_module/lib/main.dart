import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.deepPurple,
        accentColor: Colors.deepPurpleAccent,
      ),
      home: MyHomePage(title: 'Feedly Flutter'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String fullName;

  @override
  initState() {
    _setupPlatformChannel();
    super.initState();
  }

  static const platform = const MethodChannel('feedly/registration_channel');

  Future<void> _setupPlatformChannel() async {
    try {
      platform.setMethodCallHandler((call) {
        if (call.method == "register_user") {
          if (call.arguments != null) {
            setState(() {
              fullName = call.arguments.toString();
            });
          }
        }
      });
    } on PlatformException catch (e) {
      print(e);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(
              'Welcome $fullName!',
            ),
            RaisedButton(
              onPressed: () async {
                await platform.invokeMethod(
                  'logout_user',
                  "logout",
                );
                Navigator.pop(context);
              },
              child: Text("Logout"),
            ),
          ],
        ),
      ),
    );
  }
}
