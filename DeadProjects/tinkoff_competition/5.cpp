#include <vector>
#include <iostream>
#include <set>
#include <map>

class Vertex;

std::vector<std::vector<Vertex*>> squares;
int n, k;

class Vertex {
public:
	int number = 0;

	std::map<std::set<int>, int> sums = {};

	Vertex(int n) : number(n) {}
};

void fill_squares(int n, int k) {
	for (int i = 0; i < n; i++) {
		squares.push_back(std::vector<Vertex*>(n, 0));
		for (int j = 0; j < n; j++) {
			squares[i][j] = new Vertex(((i+1) * (j+1)) % k);
		}
	}
}

int count_sums(int i, std::set<int> already, int current_sum) {
	if (i == squares.size()) {
		return current_sum % k ? 0 : 1;
	}
	int total_sum = 0;
	for (unsigned j = 0; j < squares.size(); j++) {
		if (already.find(j) == already.end()) {
			Vertex * el = squares[i][j];
			auto sets = el->sums;
			if (sets.find(already) == sets.end()) {
				already.insert(j);
				auto new_sum = count_sums(i + 1, already, (current_sum + el->number) % k);
				already.erase(j);
				el->sums[already] = new_sum;
				total_sum += new_sum;
			} else {
				total_sum += sets[already];
			}
		}
	}
	return total_sum;
}

int main() {
	std::cin >> n >> k;
	
	fill_squares(n, k);

	auto vec = squares;
	/*for (unsigned int i = 0; i < vec.size(); i++)
	{
		for (unsigned int j = 0; j < vec[i].size(); j++)
		{
			std::cout << vec[i][j]->number << ' ';
		}
		std::cout << std::endl;
	}*/

	std::cout << count_sums(0, std::set<int>{}, 0) << std::endl;

	return 0;
}