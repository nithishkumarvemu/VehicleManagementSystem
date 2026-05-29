const monthlySales = {
  labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
  salesData: [],
  amountData: [],
};

// Create a line chart for sales
const salesCtx = document.getElementById('salesChart').getContext('2d');
const salesChart = new Chart(salesCtx, {
  type: 'line',
  data: {
    labels: monthlySales.labels,
    datasets: [
      {
        label: 'Sales',
        data: monthlySales.salesData,
        borderColor: 'rgb(75, 192, 192)',
        fill: false,
      },
    ],
  },
  options: {
    responsive: true,
    title: {
      display: true,
      text: 'Monthly Sales',
    },
    scales: {
      xAxes: [
        {
          scaleLabel: {
            display: true,
            labelString: 'Month',
          },
        },
      ],
      yAxes: [
        {
          scaleLabel: {
            display: true,
            labelString: 'Value',
          },
        },
      ],
    },
  },
});

// Create a line chart for orders
const salesCtx2 = document.getElementById('salesChart2').getContext('2d');
const salesChart2 = new Chart(salesCtx2, {
  type: 'line',
  data: {
    labels: monthlySales.labels,
    datasets: [
      {
        label: 'Amount',
        data: monthlySales.amountData,
        borderColor: 'rgb(255, 99, 132)',
        fill: false,
      },
    ],
  },
  options: {
    responsive: true,
    title: {
      display: true,
      text: 'Monthly Amount',
    },
    scales: {
      xAxes: [
        {
          scaleLabel: {
            display: true,
            labelString: 'Month',
          },
        },
      ],
      yAxes: [
        {
          scaleLabel: {
            display: true,
            labelString: 'Value',
          },
        },
      ],
    },
  },
});

function renderChart(data) {
  for (let i = 1; i <= 12; i++) {
    var matched = false;

    data.forEach((item) => {
      var month = item.month;
      var amount = item.amount;
      var numberOfOrders = item.numberOfOrders;

      if (i == month) {
        matched = true;
        monthlySales.salesData.push(numberOfOrders);
        monthlySales.amountData.push(amount);
      }
    });

    if (!matched) {
      monthlySales.salesData.push(0);
      monthlySales.amountData.push(0);
    }
  }
  console.log(monthlySales);
  updateChart();
}

function updateChart() {
  const startMonthSelect = document.getElementById('startMonth');
  const endMonthSelect = document.getElementById('endMonth');

  startMonthIndex = startMonthSelect.value - 1;
  endMonthIndex = endMonthSelect.value - 1;

  salesChart.data['labels'] = monthlySales.labels.slice(startMonthIndex, endMonthIndex + 1);
  salesChart.data['datasets'][0]['data'] = monthlySales.salesData.slice(startMonthIndex, endMonthIndex + 1);

  salesChart2.data['labels'] = monthlySales.labels.slice(startMonthIndex, endMonthIndex + 1);
  salesChart2.data['datasets'][0]['data'] = monthlySales.amountData.slice(startMonthIndex, endMonthIndex + 1);

  salesChart.update();
  salesChart2.update();
}

document.addEventListener('DOMContentLoaded', function () {
  fetch('/admin/monthly-sales', {
    method: 'GET', // Adjust the method as needed
    headers: {
      'Content-Type': 'application/json',
    },
  })
    .then((response) => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.json();
    })
    .then((data) => {
      renderChart(data);
    })
    .catch((error) => {
      console.error('Error:', error);
    });
});
