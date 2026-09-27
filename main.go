package main

import (
	"calendrica/reingolddershowitz"
	"fmt"
)

func main() {
	cm := reingolddershowitz.NewCalendarManager()

	fmt.Println("=== CALENDRICAL CALCULATIONS (Go Version) ===")

	// Date actuelle en Grégorien
	gDate := reingolddershowitz.FromFixed(cm.CurrentRD)
	fmt.Printf("Aujourd'hui (Grégorien) : %04d-%02d-%02d (R.D. %d)\n",
		gDate.Year, gDate.Month, gDate.Day, cm.CurrentRD)

	// Affichage des informations astronomiques (Jérusalem)
	fmt.Println(cm.GetAstroSummary(reingolddershowitz.Jerusalem))

	// Navigation
	cm.NextDay()
	gDateNext := reingolddershowitz.FromFixed(cm.CurrentRD)
	fmt.Printf("Demain (Grégorien)    : %04d-%02d-%02d (R.D. %d)\n",
		gDateNext.Year, gDateNext.Month, gDateNext.Day, cm.CurrentRD)

	// Liste des calendriers gérés
	fmt.Println("\nCalendriers pris en charge :")
	for _, calType := range reingolddershowitz.Order {
		if name, ok := reingolddershowitz.CalendarNames[calType]; ok {
			fmt.Printf(" - [%2d] %s\n", calType, name)
		}
	}
}
