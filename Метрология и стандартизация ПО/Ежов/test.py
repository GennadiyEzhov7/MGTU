class Room:
    def get_info(self):
        raise NotImplementedError("Subclasses should implement this method.")

class Office(Room):
    def __init__(self, work_places_count):
        self.work_places_count = work_places_count

    def get_info(self):
        return f"Work places count: {self.work_places_count}"

class Flat(Room):
    def __init__(self, rooms_count):
        self.rooms_count = rooms_count

    def get_info(self):
        return f"Rooms count: {self.rooms_count}"

class Shop(Room):
    def __init__(self, name):
        self.name = name

    def get_info(self):
        return f"Shop name: {self.name}"

class Technical(Room):
    def __init__(self, function):
        self.function = function

    def get_info(self):
        return f"Function: {self.function}"

class Building:
    def __init__(self):
        self.rooms = []

    def add_room(self, room):
        self.rooms.append(room)

    def print_info(self):
        for i, room in enumerate(self.rooms):
            type_ = ""
            if isinstance(room, Office):
                type_ = "office"
            elif isinstance(room, Flat):
                type_ = "flat"
            elif isinstance(room, Shop):
                type_ = "shop"
            elif isinstance(room, Technical):
                type_ = "technical room"
            print(f"On {i + 1} floor placed {type_}. Info: {room.get_info()}")

    def print_count_info(self):
        office_count = 0
        flat_count = 0
        shop_count = 0
        technical_count = 0
        for room in self.rooms:
            if isinstance(room, Office):
                office_count += 1
            elif isinstance(room, Flat):
                flat_count += 1
            elif isinstance(room, Shop):
                shop_count += 1
            elif isinstance(room, Technical):
                technical_count += 1
        print(
            f"Flats count: {flat_count}\n"
            f"Offices count: {office_count}\n"
            f"Shops count: {shop_count}\n"
            f"Technical rooms count: {technical_count}\n"
            f"Common count: {len(self.rooms)}"
        )

def main():
    building = Building()
    while True:
        print(
            "1 - Add office \n"
            "2 - Add flat \n"
            "3 - Add shop \n"
            "4 - Add technical room \n"
            "5 - Print building info\n"
            "6 - Print building count info\n"
            "7 - Exit \n"
            "Choose action:"
        )
        action = int(input().strip())
        if action == 1:
            work_places_count = int(input("Enter work places count: ").strip())
            building.add_room(Office(work_places_count))
            print("\n Added!")
        elif action == 2:
            rooms_count = int(input("Enter rooms count: ").strip())
            building.add_room(Flat(rooms_count))
            print("\n Added!")
        elif action == 3:
            name = input("Enter shop name: ").strip()
            building.add_room(Shop(name))
            print("\n Added!")
        elif action == 4:
            function = input("Enter technical room function: ").strip()
            building.add_room(Technical(function))
            print("\n Added!")
        elif action == 5:
            building.print_info()
        elif action == 6:
            building.print_count_info()
        elif action == 7:
            break

if __name__ == "__main__":
    main()
