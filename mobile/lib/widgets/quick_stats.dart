import 'package:flutter/material.dart';
import '../theme/app_theme.dart';

class QuickStats extends StatelessWidget {
  final double uploadSpeed;
  final double downloadSpeed;

  const QuickStats({
    Key? key,
    required this.uploadSpeed,
    required this.downloadSpeed,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Row(
      children: [
        Expanded(
          child: Card(
            child: Padding(
              padding: EdgeInsets.all(16),
              child: Column(
                children: [
                  Icon(
                    Icons.arrow_upward,
                    color: AppTheme.emeraldGreenDark,
                    size: 28,
                  ),
                  SizedBox(height: 8),
                  Text(
                    '${uploadSpeed.toStringAsFixed(1)} MB/s',
                    style: Theme.of(context).textTheme.headlineSmall,
                  ),
                  SizedBox(height: 4),
                  Text(
                    'Upload',
                    style: Theme.of(context).textTheme.bodySmall,
                  ),
                ],
              ),
            ),
          ),
        ),
        SizedBox(width: 12),
        Expanded(
          child: Card(
            child: Padding(
              padding: EdgeInsets.all(16),
              child: Column(
                children: [
                  Icon(
                    Icons.arrow_downward,
                    color: AppTheme.emeraldGreenDark,
                    size: 28,
                  ),
                  SizedBox(height: 8),
                  Text(
                    '${downloadSpeed.toStringAsFixed(0)} MB/s',
                    style: Theme.of(context).textTheme.headlineSmall,
                  ),
                  SizedBox(height: 4),
                  Text(
                    'Download',
                    style: Theme.of(context).textTheme.bodySmall,
                  ),
                ],
              ),
            ),
          ),
        ),
      ],
    );
  }
}
