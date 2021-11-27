# C&C helper

### Application where contractors and customers meet each other for building needs.

1. Main business logic: customers announce contract with information about building or renovation what they needed.
   Contractors browse contracts and chose one that they interested in, create offer for it. Then customers browse
   suggested offers and chose the most attractive one.
2. Roles: contractor, customer
3. Customer functions: can create an application (Contract) for the needed building work (name of the work, description,
   start and end of execution, starting price), change own contract, delete own contract. Browse suggested offers for
   own contract.
4. Contractor functions: view the list of contracts; submit, modify, delete own offer.
5. Common functions: view users (pageable function); view, edit, delete own profile.

Contractors are certified to perform work (many-to-many relationship).

Приложение для потребителей и поставщиков строительных работ.

1. Общая информация: заказчики создают заявку на выполнение строительных работ. Подрядчики просматривают заявки и на
   выбранную заявку подают свое предложение. Далее заказчики просматривают поданные заявки и выбирают лучшую.
2. Роли: заказчик (customer - потребитель строительных услуг), подрядчик (contractor - исполнитель строительных работ).
3. Функции заказчика: может создать заявку (Contract) на необходимые работы (название работы, описане, начало и
   окончание, стартовая цена), изменить заявку, удалить заявку. Просмотреть поданные предложения (Offer) на выполнение
   работы.
4. Функции подрядчика: просматривать список заявок, подавать, изменять, удалять свое предложение.
5. Общие функции: просматривать заявку, предложение, юзера.

Подрядчики имеют сертификаты на выполнение работ (связь многие ко многим).