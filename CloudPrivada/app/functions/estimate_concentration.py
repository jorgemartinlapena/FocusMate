def estimate_concentration(average_pulse: int, average_movement: int):
    # Valores ideales
    ideal_pulse = 70
    ideal_movement = 15

    # Ponderaciones para pulso y movimiento
    pulse_weight = 0.6
    movement_weight = 0.4

    # Cálculo de desviaciones normalizadas
    pulse_deviation = max(0, 100 - abs(average_pulse - ideal_pulse) * 2)
    movement_deviation = max(0, 100 - abs(average_movement - ideal_movement) * 5)

    # Cálculo de concentración como combinación ponderada
    valor = (pulse_deviation * pulse_weight) + (movement_deviation * movement_weight)

    # Limitar el valor entre 0 y 100 para cumplor las condiciones de la base de datos
    return min(100, max(0, round(valor)))
