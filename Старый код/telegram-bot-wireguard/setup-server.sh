#!/bin/bash

function is_wg_service_active {                                                    
    systemctl is-active --quiet wg-quick@wg0                                                     
}  

function is_ufw_enabled {
	ufw status | grep -qw active
}

Directory="/etc/wireguard/server"
if [ ! -d "$Directory" ]; then
	if ! is_wg_service_active ; then
		echo 
 		mkdir -p "$Directory"
		wg genkey | tee /etc/wireguard/server/server.key | wg pubkey | tee /etc/wireguard/server/server.key.pub >/dev/null
		private_key=$(cat /etc/wireguard/server/server.key)
		public_key=$(cat /etc/wireguard/server/server.key.pub)
		interface=$(ip -o -4 route show to default | awk '{print $5}')
		cat <<- CONFIGFILE > /etc/wireguard/wg0.conf
		[Interface]
		Address = 10.0.0.1/24
		ListenPort = 51820
		PrivateKey = $private_key
		PostUp = iptables -A FORWARD -i %i -j ACCEPT; iptables -t nat -A POSTROUTING -o $interface -j MASQUERADE
		PostDown = iptables -D FORWARD -i %i -j ACCEPT; iptables -t nat -D POSTROUTING -o $interface -j MASQUERADE
		SaveConfig = true
		CONFIGFILE
		chmod 600 /etc/wireguard/wg0.conf
		chmod 600 /etc/wireguard/server/server.key
		wg-quick up wg0
		systemctl enable wg-quick@wg0
		sysctl -w net.ipv4.ip_forward=1
		if ! is_ufw_enabled ; then
			read -p "Enable firewall? Press Y: " -n 1 -r
			echo
			if [[ $REPLY =~ ^[Yy]$ ]]
			then
	 			ufw allow 51820/udp
				read -p "Add ssh port to firewall (otherwise, you will not be able to access the server in future)? Press Y: " -n 1 -r
				echo
				if [[ $REPLY =~ ^[Yy]$ ]]
				then
				    ufw allow ssh
				else
					echo "ssh port was not added to firewall."
				fi
				ufw enable
				ufw status verbose
			else
				echo "user denyed the firewall set up."
			fi				
		else
			echo "ufw already enabled. exiting..."
		fi
	else
		echo "wg service already active!"
		exit 1
	fi
else
	echo "$Directory path already exists!"
	exit 1
fi

echo "congradulations: server-side wireguard was initialized!"
echo "ᕙ(⇀‸↼)ᕗ"

