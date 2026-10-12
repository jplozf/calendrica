package reingolddershowitz

import (
	"fmt"
)

// Location représente un emplacement géographique avec ses coordonnées et sa zone horaire.
type Location struct {
	Name      string
	Latitude  float64
	Longitude float64
	Elevation float64
	Zone      float64
}

// Emplacements statiques prédéfinis
var (
	URBANA = NewLocation(
		"Urbana, IL, USA",
		40.1,
		-88.2,
		Mt(225.0),
		-6.0,
	)

	LOS_ANGELES = NewLocation(
		"Los Angeles, CA, USA",
		Angle(34.0, 4.0, 0.0),
		-Angle(118.0, 15.0, 0.0),
		Mt(0.0),
		-8.0,
	)
)

// NewLocation est le constructeur de Location.
func NewLocation(name string, latitude, longitude, elevation, zone float64) Location {
	return Location{
		Name:      name,
		Latitude:  latitude,
		Longitude: longitude,
		Elevation: elevation,
		Zone:      zone,
	}
}

// String retourne une représentation textuelle sous le même format que la méthode Java toString().
func (l Location) String() string {
	return fmt.Sprintf("calendrica.Location[latitude=%v,longitude=%v,elevation=%v,zone=%v]",
		l.Latitude, l.Longitude, l.Elevation, l.Zone)
}

// Format retourne un affichage lisible intégrant le nom de l'emplacement.
func (l Location) Format() string {
	return fmt.Sprintf("%s: lat %v long %v elev %v zone %v",
		l.Name, l.Latitude, l.Longitude, l.Elevation, l.Zone)
}

// Equals vérifie l'égalité des valeurs géographiques entre deux emplacements.
func (l Location) Equals(other Location) bool {
	return l.Latitude == other.Latitude &&
		l.Longitude == other.Longitude &&
		l.Elevation == other.Elevation &&
		l.Zone == other.Zone
}

// --- Fonctions utilitaires d'unités (équivalents de ProtoDate) ---

// Angle convertit des degrés, minutes, secondes en degrés décimaux.
func Angle(deg, min, sec float64) float64 {
	return deg + min/60.0 + sec/3600.0
}

// Mt retourne la valeur en mètres (fonction identité pour la conversion d'unité).
func Mt(m float64) float64 {
	return m
}
