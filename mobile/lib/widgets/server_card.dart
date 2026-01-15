import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

class ServerCard extends StatelessWidget {
  final String serverName;
  final int latency;
  final int load;
  final VoidCallback onTap;

  const ServerCard({
    Key? key,
    required this.serverName,
    required this.latency,
    required this.load,
    required this.onTap,
  }) : super(key: key);

  Color get latencyColor {
    if (latency < 50) return Color(0xFF00B074);
    if (latency < 100) return Color(0xFFFBBF24);
    return Color(0xFFF87171);
  }

  @override
  Widget build(BuildContext context) {
    return Card(
      child: ListTile(
        contentPadding: EdgeInsets.all(16),
        leading: Icon(
          Icons.public,
          color: AppTheme.emeraldGreenDark,
          size: 32,
        ),
        title: Text(serverName),
        subtitle: Text('${latency}ms • ${load}% load'),
        trailing: Container(
          padding: EdgeInsets.symmetric(horizontal: 12, vertical: 6),
          decoration: BoxDecoration(
            color: latencyColor.withOpacity(0.2),
            borderRadius: BorderRadius.circular(4),
          ),
          child: Text(
            '${latency}ms',
            style: TextStyle(
              color: latencyColor,
              fontWeight: FontWeight.bold,
              fontSize: 12,
            ),
          ),
        ),
        onTap: onTap,
      ),
    );
  }
}
