import 'package:flutter/material.dart';
import 'package:flutter_animate/flutter_animate.dart';
import 'package:google_fonts/google_fonts.dart';
import '../theme/app_theme.dart';
import '../widgets/connection_toggle.dart';
import '../widgets/server_card.dart';
import '../widgets/quick_stats.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  bool isConnected = false;
  String currentServer = 'Auto Best (Tokyo)';
  String currentIP = '203.0.113.42';
  double uploadSpeed = 2.4;
  double downloadSpeed = 45.0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Row(
          children: [
            Icon(Icons.shield, color: AppTheme.emeraldGreenDark),
            SizedBox(width: 8),
            Text('FRANVPN'),
          ],
        ),
        elevation: 0,
        actions: [
          IconButton(
            icon: Icon(Icons.settings),
            onPressed: () {},
          ),
        ],
      ),
      body: SingleChildScrollView(
        child: Padding(
          padding: EdgeInsets.all(20),
          child: Column(
            children: [
              // Hero Connection Toggle
              ConnectionToggle(
                isConnected: isConnected,
                onPressed: () {
                  setState(() => isConnected = !isConnected);
                },
              ).animate().fadeIn(duration: 600.ms).scale(),
              
              SizedBox(height: 32),

              // Server & Protocol Info
              Card(
                child: Padding(
                  padding: EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        'Connected Server',
                        style: Theme.of(context).textTheme.bodySmall,
                      ),
                      SizedBox(height: 8),
                      Text(
                        currentServer,
                        style: Theme.of(context).textTheme.headlineMedium,
                      ),
                      SizedBox(height: 16),
                      Text(
                        'Protocol: VLESS + gRPC',
                        style: Theme.of(context).textTheme.bodyMedium,
                      ),
                      Text(
                        'IP: $currentIP',
                        style: Theme.of(context).textTheme.bodyMedium,
                      ),
                    ],
                  ),
                ),
              ).animate(delay: 200.ms).fadeIn().slideX(begin: -0.2),

              SizedBox(height: 24),

              // Speed Stats
              QuickStats(
                uploadSpeed: uploadSpeed,
                downloadSpeed: downloadSpeed,
              ).animate(delay: 400.ms).fadeIn().slideX(begin: 0.2),

              SizedBox(height: 24),

              // Quick Server Selection
              ServerCard(
                serverName: 'USA - New York',
                latency: 85,
                load: 45,
                onTap: () {},
              ).animate(delay: 600.ms).fadeIn(),

              ServerCard(
                serverName: 'Germany - Berlin',
                latency: 42,
                load: 20,
                onTap: () {},
              ).animate(delay: 800.ms).fadeIn(),

              SizedBox(height: 24),

              // Action Buttons
              Row(
                children: [
                  Expanded(
                    child: ElevatedButton.icon(
                      icon: Icon(Icons.map),
                      label: Text('Servers'),
                      onPressed: () {},
                      style: ElevatedButton.styleFrom(
                        backgroundColor: AppTheme.emeraldGreenDark,
                      ),
                    ),
                  ),
                  SizedBox(width: 12),
                  Expanded(
                    child: ElevatedButton.icon(
                      icon: Icon(Icons.bar_chart),
                      label: Text('Stats'),
                      onPressed: () {},
                      style: ElevatedButton.styleFrom(
                        backgroundColor: AppTheme.navyBlue,
                      ),
                    ),
                  ),
                ],
              ).animate(delay: 1000.ms).fadeIn().slideY(begin: 0.2),
            ],
          ),
        ),
      ),
      bottomNavigationBar: BottomNavigationBar(
        items: [
          BottomNavigationBarItem(
            icon: Icon(Icons.home),
            label: 'Home',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.public),
            label: 'Servers',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.account_circle),
            label: 'Profiles',
          ),
          BottomNavigationBarItem(
            icon: Icon(Icons.settings),
            label: 'Settings',
          ),
        ],
        currentIndex: 0,
        type: BottomNavigationBarType.fixed,
      ),
    );
  }
}
