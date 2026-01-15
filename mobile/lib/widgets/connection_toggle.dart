import 'package:flutter/material.dart';
import 'package:flutter_animate/flutter_animate.dart';
import '../theme/app_theme.dart';

class ConnectionToggle extends StatefulWidget {
  final bool isConnected;
  final VoidCallback onPressed;

  const ConnectionToggle({
    Key? key,
    required this.isConnected,
    required this.onPressed,
  }) : super(key: key);

  @override
  State<ConnectionToggle> createState() => _ConnectionToggleState();
}

class _ConnectionToggleState extends State<ConnectionToggle>
    with SingleTickerProviderStateMixin {
  late AnimationController _animationController;
  late Animation<double> _scaleAnimation;
  late Animation<double> _rotationAnimation;

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(
      duration: Duration(milliseconds: 1500),
      vsync: this,
    );

    _scaleAnimation = Tween<double>(begin: 1.0, end: 1.1).animate(
      CurvedAnimation(parent: _animationController, curve: Curves.easeInOut),
    );

    _rotationAnimation = Tween<double>(begin: 0, end: 6.28).animate(
      CurvedAnimation(parent: _animationController, curve: Curves.linear),
    );

    if (widget.isConnected) {
      _animationController.repeat();
    }
  }

  @override
  void didUpdateWidget(ConnectionToggle oldWidget) {
    super.didUpdateWidget(oldWidget);
    if (widget.isConnected && !oldWidget.isConnected) {
      _animationController.repeat();
    } else if (!widget.isConnected && oldWidget.isConnected) {
      _animationController.stop();
    }
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: widget.onPressed,
      child: Container(
        decoration: BoxDecoration(
          shape: BoxShape.circle,
          boxShadow: [
            if (widget.isConnected)
              BoxShadow(
                color: AppTheme.emeraldGreenDark.withOpacity(0.3),
                blurRadius: 20,
                spreadRadius: 5,
              ),
          ],
        ),
        child: AnimatedBuilder(
          animation: _scaleAnimation,
          builder: (context, child) {
            return Transform.scale(
              scale: _scaleAnimation.value,
              child: Container(
                width: 180,
                height: 180,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  color: widget.isConnected
                      ? AppTheme.emeraldGreenDark
                      : Color(0xFF6B7280),
                  boxShadow: [
                    BoxShadow(
                      color: (widget.isConnected
                              ? AppTheme.emeraldGreenDark
                              : Color(0xFF6B7280))
                          .withOpacity(0.4),
                      blurRadius: 20,
                      spreadRadius: 2,
                    ),
                  ],
                ),
                child: Center(
                  child: Transform.rotate(
                    angle: widget.isConnected ? _rotationAnimation.value : 0,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Icon(
                          widget.isConnected ? Icons.lock : Icons.lock_open,
                          size: 64,
                          color: Colors.white,
                        ),
                        SizedBox(height: 12),
                        Text(
                          widget.isConnected ? 'CONNECTED' : 'DISCONNECTED',
                          style: TextStyle(
                            color: Colors.white,
                            fontWeight: FontWeight.bold,
                            fontSize: 16,
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            );
          },
        ),
      ),
    );
  }
}
